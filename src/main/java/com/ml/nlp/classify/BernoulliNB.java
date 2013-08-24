package com.ml.nlp.classify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class BernoulliNB extends NaiveBayesClassifier {
	
	public BernoulliNB() {
		
	}
	
	public BernoulliNB(TrainnedModel model) {
		super(model);
	}
	
	protected void calculatePc() {
        for (int i = 0; i < db.classifications.length; i++) {
            model.setPc(i, (double)db.filesOfC[i] / (double)db.files);
        }
    }
    

	protected void calculatePxc() {
		for (int i = 0; i < db.classifications.length; i++) {
			HashMap<String, Integer> source =  db.filesOfXC[i];
			
			for (String t : db.vocabulary) {
				Integer value = source.get(t);
				if(value == null) { // 本类别下不包含单词t
					value = 0;
				}
				model.setPxc(t, i, (double)(value + 1)/(double)(db.filesOfC[i] + 2));
			}
		}
    }
    
	
	
    protected double calcProd(final List<String> x, final int cj) {
    	double ret = 0.0;
    	HashSet<String> words = new HashSet<String>();
    	
    	for(String s: x) {
    		words.add(s);
    	}
    	
    	for(Iterator<String> iter = model.vocabulary.iterator(); iter.hasNext();) {
			String t = iter.next();
			
			if(words.contains(t)) {
				ret += Math.log(model.getPxc(t, cj));
			} else {
				ret += Math.log(1- model.getPxc(t, cj));
			}
    	}
    	
        // 再乘以先验概率
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BernoulliNB nb = new BernoulliNB();
		String fileDir = "C:\\soft\\dm\\text_classification";
		//Training model
		String[] arg = new String[] {"-t", fileDir + "\\intermediate2.db", fileDir + "\\bernoulli.model"};
		//Test accurate
		String[] arg2 = new String[] {"-r", "C:\\soft\\TrainingSet2", "GBK", fileDir + "\\bernoulli.model"};
		//classify file
		String[] arg3 = new String[] {fileDir + "\\bernoulli.model", fileDir + "\\train.txt", "GBK"};

		BernoulliNB.test(nb, arg2);
        
        
	}
}

