/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.util;
/*
 * Have 2 mode : Latin mode and CJK mode, if whole search key is
 * latin script, i.e. in the following range:
 * <ul>
 * <li>0020-007F(Basic Latin)</li>
 * <li>00A0-00FF(Latin 1 Supplement)</li>
 * <li>0100-017F(Latin Extended-A)</li>
 * <li>0180-024F(Latin Extended-B)</li>
 * <li>1E00-1EFF(Latin Extended Additional)</li>
 * <li>2C60-2C7F(Latin Extended-C)</li>
 * </ul>
 * CJK mode otherwise.
 * 
 * For latin mode, the case WILL BE ignored.
 */
import java.util.ArrayList;
import java.io.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author leolo
 */
public class KeywordList {
    private ArrayList<Keyword> list;
    
    /*
     * Parse the keyword list, one keyword on one line.
     */
    public KeywordList(File f) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
        final char[] BOM_MARK = {0xfeff}; 
        while(true){
            String line = br.readLine();
            if(line == null){
                break;
            }
            //Check BOM(0xEFBBBF)mark, remove them
            if(line.startsWith(new String(BOM_MARK))){
                char [] tarr = new char[line.length()-1];
                for(int i=1;i<line.length();i++){
                    tarr[i-1] = line.charAt(i);
                }
                line = new String(tarr);
            }
            //Check is it already in the list
            boolean isExist=false;
            String tline = line;
            if(KeywordType.identify(line) == KeywordType.LATIN){
                tline = line.toLowerCase(Locale.ENGLISH);
            }
            for(int i=0;i<list.size();i++){
                if(list.get(i).keyword.equals(tline)){
                    isExist = true;
                }
            }
            if(!isExist) {
                list.add(new Keyword(line));
            }else{
                System.err.println("WARNING : duplicated entry \""+line+"\"!");
            }
        }
    }
    
    public int count(){
        return list.size();
    }
    
    public Keyword get(int index){
        return list.get(index);
    }
    
}

class Keyword{
    String keyword;
    KeywordType type;
    
    Keyword(String keyword){
        this.keyword = keyword;
        type = KeywordType.identify(keyword);
        if(type == KeywordType.LATIN){
            this.keyword = this.keyword.toLowerCase(Locale.ENGLISH);
        }
    }
}