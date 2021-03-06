package com.ml.bus.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.ml.bus.model.News;
import com.ml.util.CompressUtils;
import com.ml.util.Constants;
import com.ml.util.SSHUtil;
import com.ml.util.ShortUrlUtil;

@Service
public class SyncTaskService {
	
	
	public String analyze(List<News> newsList) throws Exception {
		long start = System.currentTimeMillis();
		
		long taskId = System.currentTimeMillis();
		System.out.println("taskId：" + taskId + ", news size: " + newsList.size());

		String newFileName = Constants.newFileName + taskId;
		String newFileSeq = Constants.newFileSeq + taskId;
		String newFileVectors = Constants.newFileVectors + taskId;
		String newFileResult = Constants.newFileResult + taskId;
		String newFileResultFile = Constants.newFileResultFile + taskId;
		
		String destCompressFilePath = this.compressContent(newsList, taskId, newFileName);
		
		SSHUtil ssh = new SSHUtil();
		ssh.sshLogin(Constants.sshIP, Constants.sshUser, Constants.sshPass);
		
		// 1) upload zip file
		ssh.scpUploadFile(Constants.defaultUploadDir, destCompressFilePath, destCompressFilePath);
		
		// 2) generate scripts
		String scriptPath = this.generateClassifyScripts(taskId, destCompressFilePath,
				newFileName, newFileSeq, newFileVectors, newFileResult, newFileResultFile);
		ssh.scpUploadFile(Constants.defaultUploadDir, scriptPath, scriptPath);
		
		// 3) exec script and get the result
		ssh.sshExec("chmod a+x " + Constants.defaultUploadDir + scriptPath + "; nohup sh " + Constants.defaultUploadDir + scriptPath);
		String result = ssh.sshExec("cat " + Constants.defaultUploadDir + newFileResultFile);
		
		// 4) do clean work
		String cleanScript = this.generateCleanScripts(destCompressFilePath, scriptPath, 
				newFileName, newFileSeq, newFileVectors, newFileResult, newFileResultFile);
		
		ssh.sshExec(cleanScript);
		ssh.sshExit();
		
		FileUtils.forceDelete(new File(newFileName)); 
		FileUtils.forceDelete(new File(destCompressFilePath)); 
		FileUtils.forceDelete(new File(scriptPath)); 
		
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
	
	private String generateClassifyScripts(long taskId, String destCompressFilePath, 
			String newFileName, String newFileSeq, String newFileVectors, String newFileResult, String newFileResultFile) throws IOException {
		// 2) generate scripts
		String newFileTFIDFVectors = newFileVectors + "/" + Constants.newFileTFIDFVectors;

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
				+ " -i " + newFileTFIDFVectors + " -o " + newFileResult + Constants.scriptSeparator);
		scriptSB.append("mahout seqdumper -i " + newFileResult + " -o " + Constants.defaultUploadDir + newFileResultFile + Constants.scriptSeparator);
		System.out.println(scriptSB.toString());
		
		// 3) upload script
		String scriptPath = Constants.scriptPath + taskId;
		FileUtils.writeStringToFile(new File(scriptPath), scriptSB.toString(), Constants.defaultFileEncoding);
		
		return scriptPath;
	}
	
	private String generateCleanScripts(String destCompressFilePath, String scriptPath,
			String newFileName, String newFileSeq, String newFileVectors, String newFileResult, String newFileResultFile) {
		
		StringBuffer cleanSB = new StringBuffer();
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + newFileName + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + destCompressFilePath + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileName + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileSeq + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileVectors + Constants.scriptSeparator);
		cleanSB.append("hadoop fs -rmr " + newFileResult + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + scriptPath + Constants.scriptSeparator);
		cleanSB.append("rm -rf " + Constants.defaultUploadDir + newFileResultFile + Constants.scriptSeparator);
		
		return cleanSB.toString();
	}
	
}
