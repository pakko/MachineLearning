package com.ml.bus.service;

import org.springframework.stereotype.Service;

import com.ml.nlp.classify.BernoulliNB;
import com.ml.nlp.classify.IntermediateData;
import com.ml.nlp.classify.MultiNomialNB;
import com.ml.nlp.classify.NaiveBayesClassifier;
import com.ml.util.Constants;

@Service
public class ClassifyService {

	public void generateIntermediateData() {
		//1, uncompress corpus data
		IntermediateData tdm = new IntermediateData();
    	tdm.generate(Constants.defaultCorpusFolder, Constants.defaultCorpusEncoding, Constants.defaultIntermediateDataFile);
    	System.out.println("中间数据生成！");
	}
	
	public void trainingModel(String type) {
		NaiveBayesClassifier classifier = null;
		String[] args = null;
		if(type.equals("bernoulli")) {
			classifier = new BernoulliNB();
			args = new String[] {Constants.defaultIntermediateDataFile, Constants.defaultBernoulliModelFile};
		}
		else if(type.equals("multinomial")) {
			classifier = new MultiNomialNB();
			args = new String[] {Constants.defaultIntermediateDataFile, Constants.defaultMultinomialModelFile};
		}
		classifier.train(args[0], args[1]);
		System.out.println("训练完毕");
	}
	
	public double testAccuracyWithBernoulli(String type) {
		NaiveBayesClassifier classifier = null;
		String[] args = null;
		if(type.equals("bernoulli")) {
			classifier = new BernoulliNB();
			args = new String[] {Constants.defaultCorpusFolder, Constants.defaultCorpusEncoding, Constants.defaultBernoulliModelFile};
		}
		else if(type.equals("multinomial")) {
			classifier = new MultiNomialNB();
			args = new String[] {Constants.defaultCorpusFolder, Constants.defaultCorpusEncoding, Constants.defaultMultinomialModelFile};
		}
		double ret = classifier.getCorrectRate(args[0], args[1], args[2]);
        System.out.println("正确率为：" + ret);
        return ret;
	}
	
	public String classifyDocument(String document, String type) {
		NaiveBayesClassifier classifier = null;
		String args = null;
		if(type.equals("bernoulli")) {
			classifier = new BernoulliNB();
			args = Constants.defaultBernoulliModelFile;
		}
		else if(type.equals("multinomial")) {
			classifier = new MultiNomialNB();
			args = Constants.defaultMultinomialModelFile;
		}
		classifier.loadModel(args);

        String result = classifier.classify(document); // 进行分类

        System.out.println("此属于[" + result + "]");
        
        return result;
	}
}
