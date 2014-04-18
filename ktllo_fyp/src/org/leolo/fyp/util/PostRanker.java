/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * To rank the post according to how much information it carry.
 * @author leolo
 */
public class PostRanker implements WordCounterUser{

    KeywordList keywords;
    public static final int THREAD_COUNT = 2;
    private int kwsReturned = 0;
    private int kwsValue = 0;
    private int wctValue = -1;
    private int emoPoint = Integer.MIN_VALUE;
    private int emoCount = -1;
    private Exception ex = null;
    private StopwordRemoverResult dataWithoutStopword=null;
    
    /**
     * To create an instance of PostRanker for a spefic keyword list
     * @param keywords File that containing the keyword, ONE keyword per line
     * @throws IOException 
     */
    public PostRanker(File keywords) throws IOException {
        this.keywords = new KeywordList(keywords);
    }
    
    /**
     * Rank the post according to the data it carry.
     * @param data A String carry the post to be ranked
     * @return Rank of the post, higher the number is, more information it carry
     * @throws Exception 
     */
    public int rank(String data) throws Exception {
        //Expan HTML entities(e.g. &nbsp;)
        kwsReturned = 0;
        kwsValue = 0;
        wctValue = -1;
        dataWithoutStopword=null;
        this.emoCount = -1;
        this.emoPoint = Integer.MIN_VALUE;
        //Create Keyword Match Threads
        int[] breakPoint = new int[THREAD_COUNT + 1];
        for (int i = 0; i <= THREAD_COUNT; i++) {
            breakPoint[i] = i * (keywords.count() / THREAD_COUNT);
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            KeywordSearcher ks = new KeywordSearcher(this, data, breakPoint[i], breakPoint[i + 1]);
            ks.start();
        }
        WordCountThread wct = new WordCountThread(this, data);
        wct.start();
        StopwordRemoverThread swrt = new StopwordRemoverThread(this,data);
        swrt.start();
        waitForResult();
        int adjust = 0;
        if(kwsValue != 0) {
            if((wctValue/kwsValue)<20){
                adjust = kwsValue * -23;
            }
        }
        return this.kwsValue * 250 + this.wctValue + adjust + 10 * this.dataWithoutStopword.getWordCount();
    }
    
    //To make the things more clear.
    private void waitForResult() throws Exception{
        while(true){
            if(kwsReturned == THREAD_COUNT && wctValue > 0 && dataWithoutStopword != null && this.emoCount > 0 && this.emoPoint > Integer.MIN_VALUE){
                break;
            }
            if(ex != null){
                throw ex;
            }
            synchronized(this){
                this.wait();
            }
        }
        
    }
    
    /**
     * Rank the post according to the data it carry.
     * @param file File that stores the post going to be ranked
     * @return Rank of the post, higher the number is, more information it carry
     * @throws Exception 
     */
    public int rank(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

        String line = null;
        line = br.readLine();
        while (line != null) {
            sb.append(line).append('\n');
            line = br.readLine();
        }
        return this.rank(sb.toString());
    }
    
    /**
     * A callback function that KeywordSearcher reports to
     * @param value Number of keyword matched
     */
    synchronized void kwsReport(int value) {
        kwsValue += value;
        kwsReturned++;
    }
    
    /**
     * A callback function that WordCountThread reports to
     * @param value Number of word
     */
    @Override
    public void wctReport(int value) {
        this.wctValue = value;
    }
    
    void swrtReport(StopwordRemoverResult swrr){
        this.dataWithoutStopword = swrr;
    }
    
    void emoReport(int point,int count){
        this.emoCount = count;
        this.emoPoint = point;
        synchronized(this){
            this.notify();
        }
    }
    
    /**
     * A callback function to allow other thread reports exceptions, and beong thrown on the main thread
     * @param ex 
     */
    void exception(Exception ex) {
        //ex.printStackTrace();
        //System.err.println(ex.getClass());
        this.ex = ex;
        synchronized(this){
            this.notify();
        }
    }
}
