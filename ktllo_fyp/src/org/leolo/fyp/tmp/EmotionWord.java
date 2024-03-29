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
public class EmotionWord {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new EmotionWord().run();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FindWord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public Vector<Pair> vb = new Vector<Pair>();
       public Vector<Pair> b = new Vector<Pair>();
       public Vector<Pair> n = new Vector<Pair>();
       public Vector<Pair> g = new Vector<Pair>();
       public Vector<Pair> vg = new Vector<Pair>();
    private void run() throws ClassNotFoundException{

        
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
            ResultSet result = stmt.executeQuery("SELECT AVG(`option`) AS 'OPT',content  FROM squestion,answer WHERE answer.qid=squestion.id AND `option` BETWEEN -2 AND 2 GROUP BY qid,content;");
            while(result.next()){
                String target = result.getString(2);
                StopwordRemoverThread srt = new StopwordRemoverThread(null,target);
                if(target.length()>0)
                    srt.removeStopword();
            workspace = srt.getString();
                double rating = result.getDouble(1);
                Vector<Pair> list = null;
                if(rating <-1.5)
                    list = vb;
                else if(rating <-0.5)
                    list = b;
                else if(rating < 0.5)
                    list=n;
                else if(rating < 1.5)
                    list = g;
                else
                    list = vg;
                //workspace = target;
                pointer = perviousPointer = 0;
                while(true){
                    String word = nextWord();
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
                }
            }
            conn.close();
        } catch(SQLException sqle){
            sqle.printStackTrace();
            System.exit(1);
        }
        Collections.sort(vb);
        Collections.sort(b);
        Collections.sort(n);
        Collections.sort(g);
        Collections.sort(vg);
        
        //Merge the list
        Vector<Pair> bad = new Vector<>();
        Vector<Pair> good = new Vector<>();
        
        //Put vb & b into bad & g & vg into good
        bad.addAll(vb);
        bad.addAll(b);
        good.addAll(g);
        good.addAll(vg);
        elimDup(bad);
        elimDup(good);
        //Remove word in both bucket set
        ArrayList<String> removePending = new ArrayList<>();
        for(int i=0;i<bad.size();i++){
            String target = bad.get(i).getKey();
            for(int j=0;j<good.size();j++){
                if(target.equalsIgnoreCase(good.get(j).getKey())){
                    removePending.add(target);
                }
            }
        }
        remove(vb,removePending);
        remove(b,removePending);
        remove(g,removePending);
        remove(vg,removePending);
        try{
            java.io.PrintWriter out = new java.io.PrintWriter("vbad");
            for(int i=0;i<vb.size();i++)
                out.println(vb.get(i).getKey());
            out.flush();
            out = new java.io.PrintWriter("bad");
            for(int i=0;i<b.size();i++)
                out.println(b.get(i).getKey());
            out.flush();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println();
    }
    
    private void remove(List<Pair> list,List<String> target){
        for(int i=0;i<target.size();i++){
            for(int j=0;j<list.size();j++){
                if(target.get(i).equalsIgnoreCase(list.get(j).getKey())){
                    list.remove(j);
                    break;
                }
            }
        }
    }
    
    private void elimDup(Vector<Pair> data){
        for(int i=0;i<data.size();i++){
            String target = data.get(i).getKey();
            for(int j=i+1;j<data.size();j++){
                if(target.equalsIgnoreCase(data.get(j).getKey())){
                    data.remove(j);
                    break;
                }
            }
        }
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
    
    class LPair{
        String key;
        double accumEmotion=0;
        int showCount = 0;
        
        public LPair(String key){
            this.key=key;
        }
        
        public void addEmotion(double rate){
            accumEmotion+=rate;
            showCount++;
        }
        
        public double getEmotion(){
            return accumEmotion/showCount;
        }
        
        public boolean isMatch(String target) {
            return key.equalsIgnoreCase(target);
        }
    }
}