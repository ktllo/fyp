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
import org.leolo.fyp.util.KeywordList;
import org.leolo.fyp.util.Keyword;

/**
 *
 * @author ktllo
 */
public class FindWord extends KeywordList {

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

    private void run() throws ClassNotFoundException {
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
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://home.leolo.org/label", "label", "label");
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT content FROM segement;");
            while (result.next()) {
                postList.add(result.getString(1));
            }
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(1);
        }

        while (!postList.isEmpty()) {
            workspace = postList.remove();
            StopwordRemoverThread srt = new StopwordRemoverThread(null, workspace);
            srt.removeStopword();
            workspace = srt.getString();
            pointer = perviousPointer = 0;
            while (true) {
                //Start of main working area
                String word = this.nextWord();
                if (word == null) {
                    break;
                }
                word = word.trim();
                if (word.length() == 0) {
                    continue;
                }
                Pair p = null;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isMatch(word)) {
                        p = list.get(i);
                        break;
                    }
                }
                if (p == null) {
                    p = new Pair(word);
                    list.add(p);
                }
                p.matchKey();
                //End of main working area
            }
        }

        //further classify the keywords to Chinese and English

        for (int i = 0; i < list.size(); i++) {
            Pair temp = list.get(i);
            //String tmp=temp.getKey();
            KeywordType type = KeywordType.identify(temp.getKey());
            switch (type) {
                case CJK:
                    if (temp.getKey().length() > 1) {
                        list_Chi.add(temp);
                    }
                    break;
                case LATIN:
                    list_Eng.add(temp);
                    break;
                default:
                    System.out.println("ERROR, cannot match KeywordType");
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
        int ecutoff[] = {8, 16, 32, 64};
        int ccutoff[] = {50, 75, 100, 125};
        for (int ec = 0; ec < 4; ec++) {
            for (int cc = 0; cc < 4; cc++) {
                int engc = 0;
                int chic = 0;
                //put the top 1/16 items from list_Eng to keyword list
                int n = list_Eng.size() / ecutoff[ec];
                int r = list_Eng.get(n).getValue();
                for (int k = 0; k < list_Eng.size(); k++) {
                    if (list.get(k).getValue() < r) {
                        continue;
                    } else {

                        System.out.println("Running list_Eng");
                        Keyword temp = new Keyword(list_Eng.get(k).getKey());
                        this.list.add(temp);
                        engc++;
                    }
                }

                //put the items with value >=100 from list_Chi to keyword list
                for (int p = 0; p < list_Chi.size(); p++) {
                    if (list_Chi.get(p).getValue() >= ccutoff[cc]) {
//                System.err.println(list_Chi.get(p).getKey());
                        Keyword temp2 = new Keyword(list_Chi.get(p).getKey());
//                if(temp2 == null){
//                    throw new RuntimeException();
//                }
                        this.list.add(temp2);
                        chic++;
                    } else {
                        break;
                    }
                }
                //Write them into file
                try{
                java.io.PrintWriter out = new java.io.PrintWriter("Output"+ec+"_"+cc+".txt");
                out.println("English : "+engc);
                out.println("Chinese : "+chic);
                out.println("Total   : "+(engc+chic));
                out.println("Diff    : "+Math.abs(engc-chic));
                for(int i=0;i<this.list.size();i++)
                    out.println(this.list.get(i));
                out.flush();
                out.close();
                }catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }

        //Pick up the most common words
        //Collections.sort(list);
        //Print the result to the console
        int threshold = list.get(list.size() / 16).getValue();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() < threshold) {
                break;
            }
            System.out.println("" + i + ":Count=" + list.get(i).getValue() + ";word=" + list.get(i).getKey());
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
            if (pointer >= workspace.length()) {
                return null;
            }
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

class Pair implements java.lang.Comparable<Pair> {

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
