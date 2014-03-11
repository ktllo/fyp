/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

import java.util.Locale;

/**
 *
 * @author leolo
 */
class KeywordSearcher extends Thread {
    private final PostRanker ranker;
    private int low;
    private int high;
    private String data;
    private String caselessData = null; //To speedup the function

    /**
     *
     * @param ranker Caller, used for callback
     * @param low Lower bound of search key ID
     * @param high  Upper bound of search key ID
     */
    public KeywordSearcher(PostRanker ranker, String data, int low, int high) {
        this.ranker = ranker;
        this.high = high;
        this.low = low;
        this.data = data;
    }

    @Override
    public void run() {
        int matchCount = 0;
        System.err.println(this.getName() + " started.");
        try {
            for (int i = low; i < high; i++) {
                Keyword target = ranker.keywords.get(i);
                //System.err.println(this.getName()+" trying "+target.keyword);
                if (target.type == KeywordType.LATIN) {
                    //Ignore Case
                    if (this.caselessData == null) {
                        this.caselessData = data.toLowerCase(Locale.ENGLISH);
                    }
                    if (caselessData.contains(target.keyword)) {
                        System.err.println(this.getName()+" matched "+target.keyword);
                        matchCount++;
                    }
                } else {
                    if (data.contains(target.keyword)) {
                        System.err.println(this.getName()+" matched "+target.keyword);
                        matchCount++;
                    }
                }
            }
            this.ranker.kwsReport(matchCount);
            System.err.println(this.getName() + " matched " + matchCount + " entries.");
            synchronized(this.ranker){
                this.ranker.notify();
            }
        } catch (Exception ex) {
            ranker.exception(ex);
        }
        
    }
    
}
