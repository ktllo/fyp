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
public class WordCountThread extends Thread {
    final WordCounterUser ranker;
    String data;
    public static final boolean DEBUG = true;
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
            System.out.println(type.name());
            //If it is CJK, the character itself is a WORD, reguardless what kind is last character
            //If it is a LATIN OR NUMBER, it never create a new word.
            //If it is a DELIMITER, it create a new word IF pervious character is LATIN OR NUMBER
            //If it is a FULLSTOP, it create a new word IF pervious character is LATIN
            switch(type){
                case CJK:
                    if(last != CharacterType.FIRST_CHARACTER && last != CharacterType.NEW_LINE && last != CharacterType.DELIMITER){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case LATIN:
                    if(last == CharacterType.CJK || last == CharacterType.NUMBER){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case NUMBER:
                    if(last == CharacterType.CJK || last == CharacterType.LATIN){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case DELIMITER:
                    if(last==CharacterType.LATIN || last==CharacterType.NUMBER || last == CharacterType.CJK){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case FULLSTOP:
                    if(last==CharacterType.LATIN || last==CharacterType.UNBREAK || last == CharacterType.CJK){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case NEW_LINE:
                    if(last==CharacterType.LATIN || last==CharacterType.UNBREAK || last == CharacterType.CJK || last == CharacterType.NUMBER){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
                    break;
                case OTHER:
                    if(last == CharacterType.CJK){
                        count++;
                        if(DEBUG) System.err.println(" >>> "+count);
                    }
            }
            if(DEBUG){
                if(next == '\n')System.err.print("\\n");
                else System.err.print(next);
            }
            last = type;
        }
        System.err.println(last.name());
        if(last == CharacterType.CJK){
            count++;
            if(DEBUG) System.err.println(" >>> "+count);
        }
        System.err.println("WCT:"+count);
        ranker.wctReport(count);
        synchronized(ranker){
            ranker.notify();
        }
    }
    
}
