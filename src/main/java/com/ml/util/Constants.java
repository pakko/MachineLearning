package com.ml.util;


public class Constants {
	
	public static final String separator = "/";
	public static String currentDir = Constants.class.getClassLoader().getResource("").getPath();
	
	public static final String defaultConfigFile = currentDir + separator + "default.properties";
	public static final String defaultLogConfigFile = currentDir + separator + "log4j.properties";


	public static final String defaultDataFolder = currentDir + separator + "data";
	public static final String stopWordListFolder = defaultDataFolder + separator + "stop_words_list";
	public static final String defaultCorpusFolder = "C:\\soft\\TrainingSet";
	
	public static final String defaultIntermediateDataFile = defaultDataFolder + separator + "intermediate.db";
	public static final String defaultBernoulliModelFile = defaultDataFolder + separator + "bernoulli.model";
	public static final String defaultMultinomialModelFile = defaultDataFolder + separator + "multinomial.model";
	public static final String defaultWeightedModelFile = defaultDataFolder + separator + "weighted.model";

	public static final String defaultCorpusEncoding = "GBK";


	public static final String mybatisDBConfigFile = "dbconf" + separator + "MybatisConfig.xml";
	
	public static final String newsCollectionName = "news";
	public static final String crawlPatternCollectionName = "crawlPattern";
	public static final String categoryCollectionName = "category";
	public static final String clusterCollectionName = "cluster";

	public static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
    // default work queue cache size
    public static int maxCacheWork = 300;
    // default add work wait time
    public static long addWorkWaitTime = Long.MAX_VALUE;
    // work thread pool size
    public static int workThreadNum = CPU_NUMBER / 2;
    // callback thread pool size
    public static int callbackThreadNum = CPU_NUMBER / 2;
    //close service wait time
    public static long closeServiceWaitTime = 5 * 60 * 1000;
    
    public static final String sshUser = "root";
    public static final String sshPass = "123123";
    //public static final String sshIP = "192.168.198.128";
    public static final String sshIP = "192.168.220.129";
    
    public static final int batchAnalyzeSize = 100;
    public static final String fileExt = ".txt";
    public static final String defaultFileEncoding = "UTF8";
    public static final String zipFileExt = ".zip";
    //public static final String defaultUploadDir = "/root/";
    public static final String defaultUploadDir = "/opt/";
    public static final String scriptSeparator = "\n";
    public static final String scriptPath = "script.sh";
    public static final String vectorWeight = "tfidf_update";
    public static final String vectorTokenAnalyzer = "com.chenlb.mmseg4j.analysis.MMSegAnalyzer";

    public static final String newFileName = "newfile";
    public static final String newFileSeq = "newfile-seq";
    public static final String newFileVectors = "newfile-vectors";
    public static final String newFileTFIDFVectors = "tfidf-vectors";

    public static final String corpusFileVectors = "news-vectors";
    public static final String corpusFileDictionaryFile = corpusFileVectors + "/" + "dictionary.file-0";
    public static final String corpusFileFrequencyFile = corpusFileVectors + "/" + "frequency.file-0";
    public static final String corpusFileTFVectors = corpusFileVectors + "/" + "tf-vectors";
    public static final String corpusFileModel = "model";
    public static final String corpusFileLabelIndex = "labelindex";

    public static final String newFileResult = "newfile-result";
    public static final String newFileResultFile = "result.res";

    public static final String distanceMesasure = "org.apache.mahout.common.distance.CosineDistanceMeasure";
    public static final int maxIter = 10;



}
