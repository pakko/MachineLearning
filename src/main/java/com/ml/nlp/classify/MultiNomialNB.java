package com.ml.nlp.classify;

import java.util.HashMap;
import java.util.List;

public class MultiNomialNB extends NaiveBayesClassifier {
	public MultiNomialNB() {
		
	}
	
	public MultiNomialNB(TrainnedModel model) {
		super(model);
	}
	
	protected void calculatePc() {
        for (int i = 0; i < db.classifications.length; i++) {
            model.setPc(i, (double)db.tokensOfC[i] / (double)db.vocabulary.wordCount());
        }
    }
    

	protected void calculatePxc() {
		for (int i = 0; i < db.classifications.length; i++) {
			HashMap<String, Integer> source =  db.tokensOfXC[i];
			//HashMap<String, Double> target = model.pXC[i];
			
			for (String t : db.vocabulary) {
				Integer value = source.get(t);
				if(value == null) { // 本类别下不包含单词t
					value = 0;
				}
				model.setPxc(t, i, (double)(value + 1)/(double)(db.tokensOfC[i] + db.vocabulary.wordCount()));
			}
		}
    }
    
	
	
    protected double calcProd(final List<String> x, final int cj) {
        double ret = 0.0;
        // 类条件概率连乘
        for (String xi: x) {
            // 因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
            ret += Math.log(model.getPxc(xi, cj));
        }
        // 再乘以先验概率
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MultiNomialNB nb = new MultiNomialNB();
		
		String fileDir = "C:\\soft\\dm\\text_classification";
		//Training model
		String[] arg = new String[] {"-t", fileDir + "\\intermediate2.db", fileDir + "\\multinomial.model"};
		//Test accurate
		String[] arg2 = new String[] {"-r", "C:\\soft\\TrainingSet2", "GBK", fileDir + "\\multinomial.model"};
		//classify file
		String[] arg3 = new String[] {fileDir + "\\multinomial.model", fileDir + "\\train.txt", "GBK"};

		
		MultiNomialNB.test(nb, arg2);
	}

}

