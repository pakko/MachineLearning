package com.ml.nlp.classify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TrainnedModel implements Serializable {
	private static final long serialVersionUID = -2079936128245283031L;
	
	/** 类别名. */
	public String[] classifications;
	/** 语料库中所出现过的单词. */
	Vocabulary vocabulary;	// 仅BernoulliNB在分类时calcProd()要用到
	/** 语料库的文件数目. */
    public int files;	// 仅weightedNB在分类时calcProd()要用到
    /** 整个训练语料所出现的单词的IDF. */
    public Map<String, Double> termsIDF;
    
	/** 类别的先验概率. */
	private double[] pC;
    /** 属性的类条件概率，String 的格式为 属性名#类别名. */
	private HashMap[] pXC;

	
    public TrainnedModel(int n) {
    	pC = new double[n];
    	pXC = new HashMap[n];
    	for(int i = 0; i < n; i++) {
        	pXC[i] = new HashMap<String, Double>();
        }
    }
    
    /**
     * 先验概率.
     * 
     * @param c
     *            给定的分类
     * @return 给定条件下的先验概率
     */
    public final double getPc(final int c) {
        return pC[c];
    }
    
    /**
     * 设置先验概率.
     * 
     * @param c
     *            给定的分类
     * @return 给定条件下的先验概率
     */
    public final void setPc(final int c, double p) {
        pC[c] = p;
    }
    
    
    /**
     * 获得类条件概率.
     * 
     * @param x
     *            给定的文本属性
     * @param c
     *            给定的分类
     * @return 该属性的类条件概率
     */
    public final double getPxc(final String x, final int c) {
    	Double ret = 1.0;
    	HashMap<String, Double> p = pXC[c];
        Double f = p.get(x);
        
        if (f != null) {
        	ret = f.doubleValue();
        }

        return ret;
    }
    
    
    /**
     * 设置类条件概率.
     * 
     * @param x
     *            给定的文本属性
     * @param c
     *            给定的分类
     * @return 该属性的类条件概率
     */
    public final void setPxc(final String x, final int c, double p) {
    	pXC[c].put(x, p);
    }
}

