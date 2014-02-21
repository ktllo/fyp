/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

/**
 * Type of keyword to decide should the keyword comparing be case sensitive or not.
 * @author leolo
 */
enum KeywordType {
    /**
     * A keyword is said to be in LATIN type iff all character in the keyword
     * is within the following range.
     * <ul>
     * <li>0020-007F(Basic Latin)</li>
     * <li>00A0-00FF(Latin 1 Supplement)</li>
     * <li>0100-017F(Latin Extended-A)</li>
     * <li>0180-024F(Latin Extended-B)</li>
     * <li>1E00-1EFF(Latin Extended Additional)</li>
     * <li>2C60-2C7F(Latin Extended-C)</li>
     * </ul>
     * 
     * A Keyword in <b>LATIN</b> should be compared case insensitive.
     */
    LATIN,
    /**
     * CJK, Chinese, Japanese, and Korean, include
     * all keyword DOES NOT fit into LATIN type.
     * 
     * A Keyword in <b>CJK</b> should be compared case sensitive.
     */
    CJK;
    
    /**
     * Identify which type is the String.
     * @param str String going to test
     * @return Type of the String given.
     */
    public static KeywordType identify(String str){
        for(int i=0;i<str.length();i++){
            char test = str.charAt(i);
            if(  (test >= '\u0020' && test <= '\u007f') ||
                 (test >= '\u00a0' && test <= '\u024f') ||
                 (test >= '\u1e00' && test <= '\u1eff') ||
                 (test >= '\u2c60' && test <= '\u2c7f') ){
                //This character IS LATIN
            }else{
                return CJK;
            }
        }
        return LATIN;
    }
}
