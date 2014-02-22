/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import java.util.Arrays;
import org.leolo.fyp.KeywordList;

/**
 *
 * @author leolo
 */
public class StopwordRemoverThread extends Thread implements WordCounterUser{
    
    StringBuffer workspace;
    PostRanker ranker;
    int wordCount=-1;
    private int pointer=0;//Next character to be scan
    private int perviousPointer=0;//First character of current word
    
    public StopwordRemoverThread(PostRanker ranker,String string){
        this.ranker = ranker;
        workspace = new StringBuffer(string);
    }
    
    @Override
    public void run(){
        //Do some work to remove the stopword
        do{
            String curWord = this.nextWord();
            if(KeywordType.identify(curWord) == KeywordType.LATIN){
                if(Arrays.binarySearch(KeywordList.ENGLISH, curWord, String.CASE_INSENSITIVE_ORDER) >= 0){ 
                    //Remove the stopword
                    this.removeWord();
                }
            }else{
                //Handle the non-english stopword, with different algroithm to handle them
                //TODO: Develop an algroithm to handle this case
            }
        }while(!isEnd());
        //Assume that the stopwords are removed
        String result = workspace.toString();
        WordCountThread wct = new WordCountThread(this, result);
        wct.start();
        //Await the WordCountThread finish its job
        while(true){
            if(this.wordCount >= 0){
                break;
            }
            synchronized(this){
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                   this.ranker.exception(ex);//Hand it back to its owner.
                }
            }
        }
        //Everything is ready
        synchronized(ranker){
            ranker.swrtReport(new StopwordRemoverResult(result,this.wordCount));
            ranker.notify();
        }
    }
    
    @Override
    public void wctReport(int value) {
        this.wordCount = value;
    }
    
    private String nextWord(){
        //TODO: Fill in pre-process code
        do{
            char currentChar = workspace.charAt(pointer++);
            //TODO: Fill in actual identifying code.
        }while( pointer < workspace.length());
        return null;
    }
    
    private void removeWord(){
        this.workspace.delete(this.perviousPointer, pointer);
        pointer=perviousPointer;
    }
    
    private void removeCharacter(int start,int end){
        if(start >= end) {
            throw new java.lang.IllegalArgumentException("end index must bigger than the start index.");
        }
        this.workspace.delete(start, end);
        pointer-=(end-start);
    }
    
    //Check is finish scaning the StringBuffer
    private boolean isEnd(){
        if( this.pointer >= this.workspace.length() ){
            return true;
        }
        return false;
    }
}
