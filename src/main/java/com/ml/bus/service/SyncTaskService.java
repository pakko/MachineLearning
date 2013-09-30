package com.ml.bus.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.model.News;
import com.ml.db.AnalyzerDB;
import com.ml.util.CompressUtils;
import com.ml.util.Constants;
import com.ml.util.SSHUtil;
import com.ml.util.ShortUrlUtil;

@Service
public class SyncTaskService {
	
	private String[] categoryIndex = new String[] {
			"C000007", "C000008", "C000010", "C000013",
			"C000014", "C000016", "C000020", "C000022",
			"C000023", "C000024"
	};
	
	@Autowired
	private NewsService newsService;
	
	//@PostConstruct 
    public void init(){ 
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int taskId = 0;
				while(true) {
					while(!AnalyzerDB.unAnalysizedNewsIsEmpty()) {
						System.out.println("Analyzer queue size: " + AnalyzerDB.getUnAnalysizedNewsNum());
						List<News> newsList = AnalyzerDB.unAnalysizedNewsDeQueue();
						if (newsList == null)
							continue;
						try {
							System.out.println("Begin to exec task:" + taskId);
							String result = analyze(newsList, taskId);
							processResult(newsList, result);
							System.out.println("End to exec task:" + taskId);
							taskId++;
						} catch (Exception e) {
							System.err.println("Error of analyzing, " + e.getMessage());
						}
					}
				}
			}
		});
		t.start();
	}

    private void processResult(List<News> newsList, String result) {
    	Map<String, Integer> resultMap = resolveResult(result);
		System.out.println("news size " + newsList.size());
		
		//save to db
		for(News news: newsList) {
			String key = ShortUrlUtil.shortText(news.getUrl())[0] + Constants.fileExt;
			int category = resultMap.get(key);
			news.setCategoryId(categoryIndex[category]);
			newsService.save(news);
		}
    }
	
	public String analyze(List<News> newsList, int taskId) throws Exception {
		long start = System.currentTimeMillis();
		System.out.println("taskId：" + taskId + ", news size: " + newsList.size());

		//1, mkdir
		String newFileName = Constants.newFileName + taskId;
		File dir = new File(newFileName);
		if( dir.exists() ) {
			FileUtils.forceDelete(dir); 
		}
		dir.mkdir();
		
		//2, write file
		for(News news: newsList) {
			String filePath = dir.getPath() + File.separator + ShortUrlUtil.shortText(news.getUrl())[0] + Constants.fileExt;
			//System.out.println(filePath);
			FileUtils.writeStringToFile(new File(filePath), news.getContent(), Constants.defaultFileEncoding);
		}
		
		//3, compress
		String destCompressFilePath = newFileName + Constants.zipFileExt;
		CompressUtils.compress(newFileName, destCompressFilePath);
		
		//4, send to hadoop host
		String result = null;
		
		SSHUtil ssh = new SSHUtil();
		ssh.sshLogin(Constants.sshIP, Constants.sshUser, Constants.sshPass);
		
		// 1) upload zip file
		ssh.scpUploadFile(Constants.defaultUploadDir, destCompressFilePath, destCompressFilePath);
		
		// 2) generate scripts
		String newFileSeq = Constants.newFileSeq + taskId;
		String newFileVectors = Constants.newFileVectors + taskId;
		String newFileTFVectors = newFileVectors + "/" + Constants.newFileTFVectors;
		String newFileResult = Constants.newFileResult + taskId;
		String newFileResultFile = Constants.newFileResultFile + taskId;
		String scriptPath = Constants.scriptPath + taskId;

		StringBuffer scriptSB = new StringBuffer();
		scriptSB.append("unzip " + Constants.defaultUploadDir + destCompressFilePath + " -d " + Constants.defaultUploadDir + Constants.scriptSeparator);
		scriptSB.append("hadoop fs -put " + Constants.defaultUploadDir + newFileName + " ." + Constants.scriptSeparator);
		scriptSB.append("mahout seqdirectory -ow -i " + newFileName + " -o " + newFileSeq + Constants.scriptSeparator);
		scriptSB.append("mvector -lnorm -nv -ow -wt " + Constants.vectorWeight + " -a " + Constants.vectorTokenAnalyzer
				+ " -i " + newFileSeq + " -o " + newFileVectors
				+ " -dp " + Constants.corpusFileDictionaryFile + " -dfp " + Constants.corpusFileFrequencyFile
				+ " -tfvp " + Constants.corpusFileTFVectors + Constants.scriptSeparator);
		scriptSB.append("mtest -ow -c"
				+ " -m " + Constants.corpusFileModel + " -l " + Constants.corpusFileLabelIndex
				+ " -i " + newFileTFVectors + " -o " + newFileResult + Constants.scriptSeparator);
		scriptSB.append("mahout seqdumper -i " + newFileResult + " -o " + Constants.defaultUploadDir + newFileResultFile + Constants.scriptSeparator);
		System.out.println(scriptSB.toString());
		
		// 3) upload script
		FileUtils.writeStringToFile(new File(scriptPath), scriptSB.toString(), Constants.defaultFileEncoding);
		ssh.scpUploadFile(Constants.defaultUploadDir, scriptPath, scriptPath);
		
		// 4) exec script and get the result
		ssh.sshExec("chmod a+x " + Constants.defaultUploadDir + scriptPath + "; nohup sh " + Constants.defaultUploadDir + scriptPath);
		result = ssh.sshExec("cat " + Constants.defaultUploadDir + newFileResultFile);
		
		// 5) do clean work
		StringBuffer cleanSB = new StringBuffer();
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + newFileName + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + destCompressFilePath + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileName + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileSeq + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileVectors + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileResult + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + scriptPath + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + newFileResultFile + Constants.scriptSeparator);
		ssh.sshExec(cleanSB.toString());
		FileUtils.forceDelete(dir); 
		FileUtils.forceDelete(new File(destCompressFilePath)); 
		FileUtils.forceDelete(new File(scriptPath)); 

		ssh.sshExit();
		
		long end = System.currentTimeMillis();
		long time = (end - start)/1000;
		
		System.out.println(" 耗时：" + time);
		
		return result;
	}
	
	public Map<String, Integer> resolveResult(String result) {
		String[] lines = result.split("\n");
		Map<String, Integer> resultMap = new HashMap<String, Integer>(lines.length);
		for(String line: lines) {
			String[] fields = line.split(": ");
			if(fields.length < 4)
				continue;
			resultMap.put(fields[1], Integer.parseInt(fields[3].split(":")[0].substring(1)));
		}
		System.out.println(resultMap.toString());
		return resultMap;
	}
	
}
