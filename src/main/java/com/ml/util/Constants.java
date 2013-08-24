package com.ml.util;


public class Constants {
	
	public static final String separator = "/";
	public static String currentDir = Constants.class.getClassLoader().getResource("").getPath();
	
	public static final String defaultConfigFile = currentDir + separator + "default.properties";
	public static final String defaultLogConfigFile = currentDir + separator + "log4j.properties";
	
	public static final String dbConfigFolder = currentDir + "dbconf";
	public static final String defaultDataFolder = currentDir + separator + "data";
	public static final String stopWordListFolder = defaultDataFolder + separator + "stop_words_list";
	public static final String defaultCorpusFolder = "C:\\soft\\TrainingSet";
	
	public static final String defaultIntermediateDataFile = defaultDataFolder + separator + "intermediate.db";
	public static final String defaultBernoulliModelFile = defaultDataFolder + separator + "bernoulli.model";
	public static final String defaultMultinomialModelFile = defaultDataFolder + separator + "multinomial.model";
	public static final String defaultWeightedModelFile = defaultDataFolder + separator + "weighted.model";

	public static final String defaultCorpusEncoding = "GBK";
	
	public static final String defaultDBConfigFileSuffix = ".dbconf";
	public static final String mybatisDBConfigFile = "dbconf" + separator + "MybatisConfig.xml";
	
	public static final String newsCollectionName = "news";
	public static final String crawlPatternCollectionName = "crawl_pattern";
	public static final String categoryCollectionName = "category";
	public static final String trainningNewsCollectionName = "training_news";
	public static final String trainningNewsCollectionName2 = "training_news2";
	public static final String newsTokensCollectionName = "news_tokens";

}
