/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leolo
 */
public class StopwordRemoverThread extends Thread implements WordCounterUser{
    
    StringBuffer workspace;
    PostRanker ranker;
    int wordCount=-1;
    
    public StopwordRemoverThread(PostRanker ranker,String string){
        this.ranker = ranker;
        workspace = new StringBuffer(string);
    }
    
    @Override
    public void run(){
        //Do some work to remove the stopword
        //To be fill in...
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
    
}
