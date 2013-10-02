package com.ml.bus.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.IClusterDAO;
import com.ml.bus.model.Cluster;
import com.ml.bus.model.News;
import com.ml.util.CompressUtils;
import com.ml.util.Constants;
import com.ml.util.SSHUtil;
import com.ml.util.ShortUrlUtil;


@Service
public class ClusterService {
	
	@Autowired
	IClusterDAO clusterDAO;
	
	public void save(Cluster cluster) {
		clusterDAO.save(cluster);
	}
	
	public List<Cluster> findAll() {
		return clusterDAO.findAll();
	}
	
	public List<Cluster> findByCategory(String categoryId) {
		return clusterDAO.findByCategory(categoryId);
	}
	
	public void delete(Cluster cluster) {
		clusterDAO.delete(cluster);
	}
	
	public String doCluster(List<News> newsList) throws Exception {		
		long start = System.currentTimeMillis();
		
		long taskId = System.currentTimeMillis();
		System.out.println("taskId：" + taskId + ", news size: " + newsList.size());
		
		String newFileName = Constants.newFileName + taskId;
		String newFileSeq = Constants.newFileSeq + taskId;
		String newFileVectors = Constants.newFileVectors + taskId;
		String clusterResult = "cdump.txt" + taskId;

		int numOfClusters = (newsList.size() / 10) == 0 ? 1: (newsList.size() / 10);
		String centroid = "kmeans-centroids" + taskId;
		String clusters = "kmeans-clusters" + taskId;
		
		String destCompressFilePath = this.compressContent(newsList, taskId, newFileName);
		
		SSHUtil ssh = new SSHUtil();
		ssh.sshLogin(Constants.sshIP, Constants.sshUser, Constants.sshPass);
		
		// 1) upload zip file
		ssh.scpUploadFile(Constants.defaultUploadDir, destCompressFilePath, destCompressFilePath);
		
		// 2) generate scripts
		String scriptPath = this.generateClusterScripts(taskId, destCompressFilePath,
				newFileName, newFileSeq, newFileVectors, clusterResult,
				numOfClusters, centroid, clusters);
		ssh.scpUploadFile(Constants.defaultUploadDir, scriptPath, scriptPath);
		
		// 3) exec script and get the result
		ssh.sshExec("chmod a+x " + Constants.defaultUploadDir + scriptPath + "; nohup sh " + Constants.defaultUploadDir + scriptPath);
		ssh.scpDownloadFile(Constants.defaultUploadDir, clusterResult, clusterResult);
		
		// 4) do clean work
		String cleanScript = this.generateCleanScripts(destCompressFilePath, scriptPath, 
				newFileName, newFileSeq, newFileVectors, clusterResult,
				centroid, clusters);
		
		ssh.sshExec(cleanScript);
		ssh.sshExit();
		
		FileUtils.forceDelete(new File(newFileName)); 
		FileUtils.forceDelete(new File(destCompressFilePath)); 
		FileUtils.forceDelete(new File(scriptPath)); 
		
		long end = System.currentTimeMillis();
		long time = (end - start)/1000;
		
		System.out.println(" 耗时：" + time);
		return clusterResult;
	}
	
	public static void main(String[] atr) throws IOException {
		ClusterService cs = new ClusterService();
		cs.resolveResult("d:/cp.res");
	}
	
	public Map<Integer, List<String>> resolveResult(String resultFile) throws IOException {
		//Key: 25: Value: 1.0: /63M7F3.txt = []
		List<String> lines = FileUtils.readLines(new File(resultFile), Constants.defaultFileEncoding);
		Map<Integer, List<String>> resultMap = new HashMap<Integer, List<String>>(lines.size());
		for(String line: lines) {
			String[] fields = line.split(": ");
			if(fields.length < 5)
				continue;
			int key = Integer.parseInt(fields[1]);
			String value = fields[4].substring(1, fields[4].indexOf(" = "));
			
			List<String> valueList = resultMap.get(key);
			if(valueList == null) {
				valueList = new ArrayList<String>();
			}
			valueList.add(value);
			resultMap.put(key, valueList);
		}
		FileUtils.forceDelete(new File(resultFile)); 
		return resultMap;
	}
	
	private String compressContent(List<News> newsList, long taskId, String newFileName) throws Exception {
		//1, mkdir
		File dir = new File(newFileName);
		if( dir.exists() ) {
			FileUtils.forceDelete(dir); 
		}
		dir.mkdir();
		
		//2, write file
		for(News news: newsList) {
			String filePath = dir.getPath() + File.separator + ShortUrlUtil.shortText(news.getUrl())[0] + Constants.fileExt;
			FileUtils.writeStringToFile(new File(filePath), news.getContent(), Constants.defaultFileEncoding);
		}
		
		//3, compress
		String destCompressFilePath = newFileName + Constants.zipFileExt;
		CompressUtils.compress(newFileName, destCompressFilePath);
		
		return destCompressFilePath;
	}
	
	private String generateClusterScripts(long taskId, String destCompressFilePath, 
			String newFileName, String newFileSeq, String newFileVectors, String clusterResult,
			int numOfClusters, String centroid, String clusters) throws IOException {
		// 2) generate scripts
		String newFileTFIDFVectors = newFileVectors + "/" + Constants.newFileTFIDFVectors;
		//String clustersFinal = clusters + "/clusters-*-final";
		String clusteredPoints = clusters + "/clusteredPoints";
		
		StringBuffer scriptSB = new StringBuffer();
		scriptSB.append("unzip " + Constants.defaultUploadDir + destCompressFilePath + " -d " + Constants.defaultUploadDir + Constants.scriptSeparator);
		scriptSB.append("hadoop fs -put " + Constants.defaultUploadDir + newFileName + " ." + Constants.scriptSeparator);
		scriptSB.append("mahout seqdirectory -ow -i " + newFileName + " -o " + newFileSeq + Constants.scriptSeparator);
		scriptSB.append("mvector -lnorm -nv -ow -wt " + Constants.vectorWeight + " -a " + Constants.vectorTokenAnalyzer
				+ " -i " + newFileSeq + " -o " + newFileVectors
				+ " -dp " + Constants.corpusFileDictionaryFile + " -dfp " + Constants.corpusFileFrequencyFile
				+ " -tfvp " + Constants.corpusFileTFVectors + Constants.scriptSeparator);
		scriptSB.append("mahout kmeans -cl -ow -i " + newFileTFIDFVectors + " -c " + centroid + " -o " + clusters + 
				" -k " + numOfClusters + " -x " + Constants.maxIter + " -dm " + Constants.distanceMesasure + Constants.scriptSeparator);
		//scriptSB.append("mahout clusterdump -i " + clustersFinal + " -p " + clusteredPoints + " -o " + Constants.defaultUploadDir + clusterResult + Constants.scriptSeparator);
		scriptSB.append("mahout seqdumper -i " + clusteredPoints + " -o " + Constants.defaultUploadDir + clusterResult + Constants.scriptSeparator);
		System.out.println(scriptSB.toString());

		// 3) upload script
		String scriptPath = Constants.scriptPath + taskId;
		FileUtils.writeStringToFile(new File(scriptPath), scriptSB.toString(), Constants.defaultFileEncoding);
		
		return scriptPath;
	}
	
	private String generateCleanScripts(String destCompressFilePath, String scriptPath,
			String newFileName, String newFileSeq, String newFileVectors, String clusterResult,
			String centroid, String clusters) {
		
		StringBuffer cleanSB = new StringBuffer();
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + newFileName + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + destCompressFilePath + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileName + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileSeq + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileVectors + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + centroid + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + clusters + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + scriptPath + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + clusterResult + Constants.scriptSeparator);
		System.out.println(cleanSB.toString());
		return cleanSB.toString();
	}
	
}
