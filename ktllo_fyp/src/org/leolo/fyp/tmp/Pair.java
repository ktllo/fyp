/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.tmp;

/**
 *
 * @author leolo
 */
public class Pair implements Comparable<Pair> {
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
        return Integer.compare(t.value, value);
    }

    public String getKey() {
        return this.key;
    }
    
}
