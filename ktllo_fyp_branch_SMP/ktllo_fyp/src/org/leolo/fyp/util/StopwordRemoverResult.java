/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

/**
 *
 * @author leolo
 */
public class StopwordRemoverResult {
    private String str;
    private int wordCount;
    
    public StopwordRemoverResult(String str,int wordCount){
        this.str=str;
        this.wordCount = wordCount;
    }
    
    public String getString(){
        return str;
    }
    
    public int getWordCount(){
        return wordCount;
    }
    
    @Override
    public String toString(){
        return wordCount+" for "+str;
    }
}
