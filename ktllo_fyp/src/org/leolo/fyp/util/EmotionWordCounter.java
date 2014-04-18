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
    private EmotionWord word;
    private String target;
    private PostRanker ranker;
    
    private int count = 0;
    private int point = 0;
    
    public EmotionWordCounter(PostRanker ranker,String target,EmotionWord word){
        this.target = target;
        this.word = word;
        this.ranker = ranker;
    }
    
    @Override
    public void run(){
        checkList(word.vb,-2);
        checkList(word.b,-1);
        checkList(word.g,1);
        checkList(word.vg,2);
        this.ranker.emoReport(point, count);
    }
    
    public void checkList(Vector<Pair> list, int point){
        for(int i=0;i<list.size();i++){
            String word = list.get(i).getKey().trim();
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
