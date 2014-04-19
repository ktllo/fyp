/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

/**
 *
 * @author leolo
 */
public class RankingResult {
    private int keyword;
    private int emotionWord;
    private int word;
    private int wordWithoutStopword;
    private int emotionPoint;
    private int totalPoint;
    
    public RankingResult(int keyword,int emotionWord , int word,int wordWithoutStopword,int emotionPoint){
        this.keyword=keyword;
        this.emotionWord=emotionWord;
        this.word=word;
        this.wordWithoutStopword=wordWithoutStopword;
        this.emotionPoint=emotionPoint;
        this.totalPoint=((this.keyword*40) + (this.emotionWord*20) + 
                (this.wordWithoutStopword*3) + (this.word*1) );
    }
    
    public int getKeyword(){
        return this.keyword;
    }
    
    public int getEmotionWord(){
        return this.emotionWord;
    }
    
    public int getWord(){
        return this.word;
    }
    
    public int getWordWithoutStopword(){
        return this.wordWithoutStopword;
    }
    
    public int getEmotionPoint(){
        return this.emotionPoint;
    }
    
    public int getTotalPoint(){
        return this.totalPoint;
    }
            
                
            
}
