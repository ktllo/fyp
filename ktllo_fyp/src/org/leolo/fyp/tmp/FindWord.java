/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.tmp;

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.leolo.fyp.util.CharacterType;
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
        try {
            new FindWord().run();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FindWord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void run() throws ClassNotFoundException{
        Queue<String> postList = new LinkedList<String>();
        //Add extra declaration here
        Vector<Pair> list = new Vector<Pair>();
        
        List<Pair> list_Eng = new ArrayList<Pair>();
        List<Pair> list_Chi = new ArrayList<Pair>();
        
        try {
            //Make a DB connection and get ALL post seems to be in English
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://home.leolo.org/label", "label", "label");
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT content FROM segement;");
            while(result.next()){
                postList.add(result.getString(1));
            }
            conn.close();
        } catch(SQLException sqle){
            sqle.printStackTrace();
            System.exit(1);
        }
        
        while(!postList.isEmpty()){
            workspace = postList.remove();
            StopwordRemoverThread srt = new StopwordRemoverThread(null,workspace);
            srt.removeStopword();
            workspace = srt.getString();
            pointer = perviousPointer = 0;
            while(true){
                //Start of main working area
                String word = this.nextWord();
                if(word == null)
                    break;
                if(word.length() ==0) continue;
                Pair p = null;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isMatch(word)){
                        p = list.get(i);
                        break;
                    }
                }
               if( p == null ){
                   p = new Pair(word);
                   list.add(p);
               }
               p.matchKey();
               //End of main working area
            }
        }
        
        //further classify the keywords to Chinese and English
        
        for(int i=0;i<list.size();i++)
        {
            Pair temp = list.get(i);
            //String tmp=temp.getKey();
            KeywordType type=KeywordType.identify(temp.getKey());
            switch(type)
            {
                case CJK: list_Chi.add(temp); break;
                case LATIN: list_Eng.add(temp); break;
                default: System.out.println("ERROR, cannot match KeywordType");
                         System.exit(1);
            }
            
        }
        /* for testing the content of the 2 list
        for (int k=0;k<list_Eng.size();k++)
        {
            System.out.println(list_Eng.get(k));
        }
        
         for (int k=0;k<list_Chi.size();k++)
        {
            System.out.println(list_Chi.get(k));
        }
                */
        
        //sort the list_Chi and list_Eng
        Collections.sort(list_Chi);
        Collections.sort(list_Eng);
        
        
        //Pick up the most common words
        //Collections.sort(list);
        //Print the result to the console
        int threshold = list.get(list.size()/16).getValue();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getValue() < threshold) break;
            System.out.println(""+i+":Count="+list.get(i).getValue()+";word="+list.get(i).getKey());
        }
        System.out.println(list.size());
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
            if(pointer >= workspace.length() ) return null;
            char currentChar = workspace.charAt(pointer);
            type = CharacterType.identify(currentChar);
            switch (type) {
                case NUMBER:
                case FULLSTOP:
                case DELIMITER:
                case NEW_LINE:
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
        return Integer.compare(t.value, value);
    }
   
    public String getKey(){
        return this.key;
    }
}
