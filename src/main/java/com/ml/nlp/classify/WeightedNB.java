package com.ml.nlp.classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ml.util.MathUtil;


public class WeightedNB extends NaiveBayesClassifier {
	
	public WeightedNB() {
		
	}
	
	public WeightedNB(TrainnedModel model) {
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
			
			for (String t : db.vocabulary) {
				Integer value = source.get(t);
				if(value == null) { // 本类别下不包含单词t
					value = 0;
				}
				//model.setPxc(t, i, (double)(value + 1)/(double)(db.tokensOfC[i] + db.vocabulary.wordCount()));
				model.setPxc(t, i, (double)(value + 1)/(double)(db.tokensOfC[i]));
			}
		}
    }
    public List<double[]> list = new ArrayList<double[]>();
    protected double calcProd(final List<String> x, final int cj) {
        double ret = 0.0;
        
        // 先计算后验概率，即TD-IDF值
        double[] features = new double[model.termsIDF.size()];
        int i = 0;
        for (String term : model.termsIDF.keySet()) {
        	double tf = 0.0;
			if(x.contains(term)) {
				tf = model.getPxc(term, cj);
			}
        	double idf = model.termsIDF.containsKey(term) ? model.termsIDF.get(term) : Math.log(model.files);
			features[i] =  tf * idf;
			i++;
		}
		
		// 向量标准化，向量坐标都除于向量的长度 
		// 如向量{1,2,3}，长度是√1²+2²+3²=√14，标准化之后是 {1/√14,2/√14,3/√14}，新向量的长度恰好为1 
		features = MathUtil.normalize(features);
		
		list.add(features);
		//saveFeatures(features);
		ret += this.weightSum(features);
		
        // 再乘以先验概率
        ret += Math.log(model.getPc(cj));
        return ret;
    }
    
    private void saveFeatures(double[] features) {
    	StringBuffer sb = new StringBuffer();
    	for(double feature: features) {
    		sb.append(feature + ",");
    	}
    	write("d:\\sogou\\features.txt", sb.toString());
		
	}

	public static void write(String path, String content) {
		String s = new String();
		String s1 = new String();
		try {
			File f = new File(path);
			if (f.exists()) {
				System.out.println("文件存在");
			} else {
				System.out.println("文件不存在，正在创建...");
				if (f.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
				}
			}
			BufferedReader input = new BufferedReader(new FileReader(f));
			while ((s = input.readLine()) != null) {
				s1 += s + "\n";
			}
			System.out.println("文件内容：" + s1);
			input.close();
			s1 += content;
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	private double weightSum(double[] features) {
		double ret = 0.0;
		for(double feature: features) {
			if(feature > 0) {
				ret += Math.log(feature);
			}
		}
		return ret;
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WeightedNB nb = new WeightedNB();
		String fileDir = "d:\\sogou";
		//Training model
		String[] arg = new String[] {"-t", fileDir + "\\intermediate2.db", fileDir + "\\weighted.model"};
		//Test accurate
		String[] arg2 = new String[] {"-r", "d:\\sogou\\TrainingSet2", "GBK", fileDir + "\\weighted.model"};
		//classify file
		String[] arg3 = new String[] {fileDir + "\\weighted.model", fileDir + "\\train.txt", "GBK"};

		WeightedNB.test(nb, arg2);
		List<double[]> list = nb.list;
		StringBuffer sb = new StringBuffer();

		for(double[] ff: list) {
	    	for(double feature: ff) {
	    		sb.append(feature + ",");
	    	}
	    	sb.append("\n");
		}
		write("d:\\sogou\\features.txt", sb.toString());
        
	}
}