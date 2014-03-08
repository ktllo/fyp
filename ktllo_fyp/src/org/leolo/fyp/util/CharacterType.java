/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;

/**
 * The CharacterType used within this class.
 */
 public enum CharacterType {
    /**
     * Default value. Never being match.
     */
    FIRST_CHARACTER, /**
     * <b>NUMBER</b> is character with range (U+0030~U+0039)
     */ NUMBER, /**
     * <b>FULLSTOP</b> is character U+002E
     */ FULLSTOP, /**
     * The following character is <b>DELIMITER</b>
     * <ul>
     * <li>0009</li>
     * <li>000A</li>
     * <li>000D</li>
     * <li>0020-0026</li>
     * <li>0028-002C</li>
     * <li>002F</li>
     * <li>003A-0040</li>
     * <li>005B-005F</li>
     * <li>007B-007E</li>
     * <li>00A0(No-break space)</li>
     * <li>2018-2019(Paired Quotation mark)</li>
     * <li>201C-201D(Paired Quotation mark)</li>
     * </ul>
     */ DELIMITER,
     /**
      * A <b>UNBREAK</b> character is either a hyphen(-) or a single quote(').
      */
        UNBREAK,
     /**
     * A <b>LATIN</b> character is character in following range but not fit into <b>DELIMITER</b>,<b>UNBREAK</b>,<b>FULLSTOP</b> or <b>NUMBER</b> type.
     * <ul>
     * <li>0020-007F(Basic Latin)</li>
     * <li>00A0-00FF(Latin 1 Supplement)</li>
     * <li>0100-017F(Latin Extended-A)</li>
     * <li>0180-024F(Latin Extended-B)</li>
     * <li>1E00-1EFF(Latin Extended Additional)</li>
     * <li>2C60-2C7F(Latin Extended-C)</li>
     * </ul>
     */ LATIN, /**
      * A <b>CJK</b> character is character within following range:
      * <ul>
      * <li>31C0-31EF(中日韓筆畫,	CJK Strokes)</li>
      * <li>31F0-31FF(日文片假名語音擴充功能,Katakana Phonetic Extensions)</li>
      * <li>3300-33FF(中日韓相容,CJK Compatibility)</li>
      * <li>3400-4DBF(中日韓統一表意文字擴充功能A,CJK Unified Ideographs Extension A)</li>
      * <li>4DC0-4DFF(易經六十四卦符號,Yijing Hexagrams Symbols</li>
      * <l>4E00-9FFF(中日韓統一表意文字,CJK Unified Ideographs)</li>
      * </ul>
     */ CJK,
     /** If a character does not belongs to any type, it will belongs to 
      * <b>OTHER</b> type, <i>i.e.</i> this is a catch-all type
      */
        OTHER;

    /**
     * Identify which class the character is in.
     * @param c Character going to be identified
     * @return Class the character is in
     */
    public static CharacterType identify(char c) {
        System.out.println(Integer.toHexString(c));
        if ( c == '-' || c == '\'' ){
            return UNBREAK;
        }
        if (c >= '\u0030' && c <= '\u0039') {
            return NUMBER;
        }
        if (c == '\u002e') {
            return FULLSTOP;
        }
        if ((c == '\u0009')
                || (c == '\n')                          //New Line
                || (c == '\r')                          //New Line
                || (c == '/')                      // Forward Slash
                || (c >= '\u0020' && c <= '\u002C')
                || (c >= '\u003A' && c <= '\u0040')
                || (c >= '\u005B' && c <= '\u005F') 
                || (c >= '\u007B' && c <= '\u007E')
                || (c >= '\u2018' && c <= '\u2019')
                || (c >= '\u201C' && c <= '\u201D')
                || (c == '\u00a0')) {
            return DELIMITER;
        }
        if ((c >= '\u0020' && c <= '\u007f') || (c >= '\u00a0' && c <= '\u024f') || (c >= '\u1e00' && c <= '\u1eff') || (c >= '\u2c60' && c <= '\u2c7f')) {
            return LATIN;
        }
        if ((c >= '\u31c0' && c <= '\u9fff')){
            return CJK;
        }
        return OTHER;
    }
    
}
