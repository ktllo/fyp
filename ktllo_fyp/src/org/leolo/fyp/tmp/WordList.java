/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.tmp;

import java.util.Vector;

/**
 *
 * @author leolo
 */
class WordList {
    private Vector<String> words;
    private int length;

    public WordList(int length) {
        this.length = length;
        words = new Vector<String>();
    }

    public void offer(String word) throws IllegalArgumentException {
        if (word.length() != this.length) {
            throw new IllegalArgumentException();
        }
        this.words.add(word);
    }

    public int getLength() {
        return this.length;
    }

    public String[] toArray(String[] a) {
        return words.toArray(a);
    }
    
}
