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
    
    /**
     * The CharacterType used within this class.
     */
    enum CharacterType {
        
        /**
         * Default value. Never being match.
         */
        FIRST_CHARACTER,
        /**
         * <b>NUMBER</b> is character with range (U+0030~U+0039)
         */
        NUMBER, /**
         * <b>FULLSTOP</b> is character U+002E
         */ FULLSTOP, /**
         * The following character is <b>DELIMITER</b>
         * <ul>
         * <li>0009</li>
         * <li>000A</li>
         * <li>000D</li>
         * <li>0020-002C</li>
         * <li>002F</li>
         * <li>003A-0040</li>
         * <li>005B-005F</li>
         * <li>007B-007E</li>
         * <li>00A0(No-break space)</li>
         * <li>2018-2019(Paired Quotation mark)</li>
         * <li>201C-201D(Paired Quotation mark)</li>
         * </ul>
         */ DELIMITER, /**
         * A <b>LATIN</b> character is character in following range but not fit into <b>DELIMITER</b>,<b>FULLSTOP</b> or <b>NUMBER</b> type.
         * <ul>
         * <li>0020-007F(Basic Latin)</li>
         * <li>00A0-00FF(Latin 1 Supplement)</li>
         * <li>0100-017F(Latin Extended-A)</li>
         * <li>0180-024F(Latin Extended-B)</li>
         * <li>1E00-1EFF(Latin Extended Additional)</li>
         * <li>2C60-2C7F(Latin Extended-C)</li>
         * </ul>
         */ LATIN, /**
         * Character does not fit into any other type is <b>CJK</b>
         */ CJK;

        /**
         * Identify which class the character is in.
         * @param c Character going to be identified
         * @return Class the character is in
         */
        public static CharacterType identify(char c) {
            if (c >= '\u0030' && c <= '\u0039') {
                return NUMBER;
            }
            if ( c == '\u002e'){
                return FULLSTOP;
            }
            if ( (c == '\u0009') ||
                 (c == '\n') || //U+000A
                 (c == '\r') || //U+000D
                 (c == '\u002f') ||
                 (c >= '\u0020' && c <= '\u002C') ||
                 (c >= '\u003A' && c <= '\u0040') ||
                 (c >= '\u005B' && c <= '\u005F') ||
                 (c >= '\u007B' && c <= '\u007E') ||
                 (c >= '\u2018' && c <= '\u2019') ||
                 (c >= '\u201C' && c <= '\u201D') ||
                 (c == '\u00a0')){
                return DELIMITER;
            }
            if(  (c >= '\u0020' && c <= '\u007f') ||
                 (c >= '\u00a0' && c <= '\u024f') ||
                 (c >= '\u1e00' && c <= '\u1eff') ||
                 (c >= '\u2c60' && c <= '\u2c7f') ){
                return LATIN;
            }
            return CJK;
        }
    }
    
}
