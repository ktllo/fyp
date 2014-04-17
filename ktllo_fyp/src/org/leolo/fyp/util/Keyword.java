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
public class Keyword {
    String keyword;
    KeywordType type;

    Keyword(String keyword) {
        this.keyword = keyword;
        type = KeywordType.identify(keyword);
        if (type == KeywordType.LATIN) {
            this.keyword = this.keyword.toLowerCase(Locale.ENGLISH);
        }
    }
    
}
