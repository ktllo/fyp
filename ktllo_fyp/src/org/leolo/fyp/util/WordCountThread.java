/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

/**
 * This thread will count the number of word in the given String.
 *
 * @author leolo
 */
class WordCountThread extends Thread {
    final WordCounterUser ranker;
    String data;
    
    /**
     * Create an instance of <b>WordCountThread</b>.
     * @param ranker The PostRanker instance which this thread should reports to
     * @param data  The textual data for this thread to be runs on
     */
    public WordCountThread(WordCounterUser ranker, String data) {
        this.ranker = ranker;
        this.data = data;
    }
    
    public void run() {
        CharacterType last = CharacterType.FIRST_CHARACTER;
        int count = 0;
        for(int i=0;i<data.length();i++){
            char next = data.charAt(i);
            final CharacterType type = CharacterType.identify(next);//Avoid accidental overwrite
            //If it is CJK, the character itself is a WORD, reguardless what kind is last character
            //If it is a LATIN OR NUMBER, it never create a new word.
            //If it is a DELIMITER, it create a new word IF pervious character is LATIN OR NUMBER
            //If it is a FULLSTOP, it create a new word IF pervious character is LATIN
            switch(type){
                case CJK:
                    count++;
                    break;
                case LATIN:
                case NUMBER:
                    break;
                case DELIMITER:
                    if(last==CharacterType.LATIN || last==CharacterType.NUMBER ){
                        count++;
                    }
                    break;
                case FULLSTOP:
                    if(last==CharacterType.LATIN){
                        count++;
                    }
                    break;
            }
            last = type;
        }
        System.err.println("WCT:"+count);
        ranker.wctReport(count);
        synchronized(ranker){
            ranker.notify();
        }
    }
    
}
