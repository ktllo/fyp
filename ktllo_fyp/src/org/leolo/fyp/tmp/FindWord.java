/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.tmp;

import org.leolo.fyp.util.CharacterType;
import static org.leolo.fyp.util.CharacterType.CJK;
import static org.leolo.fyp.util.CharacterType.DELIMITER;
import static org.leolo.fyp.util.CharacterType.FULLSTOP;
import static org.leolo.fyp.util.CharacterType.LATIN;
import static org.leolo.fyp.util.CharacterType.NUMBER;
import org.leolo.fyp.util.KeywordType;
import org.leolo.fyp.util.StopwordRemoverThread;

/**
 *
 * @author ktllo
 */
public class FindWord {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new FindWord().run();
    }

    private void run() {
        
        //Build a data struct to keep the record
        //For each post do
        ////Read in the post
        ////Remove the stopword
        ////For each word do
        //////Update the record
        //Pick up the most common words
        //Print the result to the console
    }
    private int pointer = 0;//Next character to be scan
    private String workspace;
    private int perviousPointer = 0;//First character of current word

    private String nextWord() {
        //TODO: Fill in pre-process code
        perviousPointer = pointer;
        CharacterType pervious = CharacterType.FIRST_CHARACTER;
        boolean haveChar = false;
        boolean wordFormed = false;
        CharacterType type = null;
        do {
            char currentChar = workspace.charAt(pointer);
            type = CharacterType.identify(currentChar);
            switch (type) {
                case NUMBER:
                case FULLSTOP:
                case DELIMITER:
                    if (haveChar) {
                        wordFormed = true;
                    } else {
                        perviousPointer++;
                    }
                    break;
                case LATIN:
                    haveChar = true;
                    if (pervious == CharacterType.CJK) {
                        wordFormed = true;
                    }
                    break;
                case CJK:
                    haveChar = true;
                    if (pervious == CharacterType.LATIN) {
                        wordFormed = true;
                    }
            }
            if (wordFormed) {
                return workspace.substring(perviousPointer, pointer);
            }
            pervious = type;
            if (++pointer >= workspace.length()) {
                try {
                    return workspace.substring(perviousPointer, pointer);
                } catch (StringIndexOutOfBoundsException sioobe) {
                    return null;
                }
            }
        } while (true);
        //return null;
    }
}

class Pair implements java.lang.Comparable<Pair>{

    private String key;
    private int value = 0;

    public Pair(String key) {
        this.key = key;
    }

    public void matchKey() {
        value++;
    }

    public int getValue() {
        return value;
    }

    public boolean isMatch(String target) {
        return key.equalsIgnoreCase(target);
    }

    @Override
    public int compareTo(Pair t) {
        return Integer.compare(value, t.value);
    }
}
