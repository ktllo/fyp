/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import org.leolo.fyp.tmp.*;
import java.util.*;

/**
 *
 * @author leolo
 */
public class EmotionWordCounter extends Thread{
    private String target;
    private PostRanker ranker;
    
    private int count = 0;
    private int point = 0;
    
    public EmotionWordCounter(PostRanker ranker,String target){
        this.target = target;
        this.ranker = ranker;
    }
    
    @Override
    public void run(){
        checkList(org.leolo.fyp.tmp.EmotionWordList.vb,-2);
        checkList(org.leolo.fyp.tmp.EmotionWordList.b,-1);
        checkList(org.leolo.fyp.tmp.EmotionWordList.g,1);
        checkList(org.leolo.fyp.tmp.EmotionWordList.vg,2);
        this.ranker.emoReport(point, count);
    }
    
    public void checkList(String [] list, int point){
        for(int i=0;i<list.length;i++){
            String word = list[i].trim();
            for(int j=0;j<target.length()-word.length();j++){
                String sstr = target.substring(j,j+word.length());
                if(sstr.equalsIgnoreCase(word)){
                    this.count++;
                    this.point+=point;
                }
            }
        }
    }
    
}
