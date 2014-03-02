package org.leolo.fyp.tmp;


import java.io.*;
import java.util.Arrays;
import java.util.Vector;
/**
 *
 * @author leolo
 */
public class DataFetcher {
    public static void main(String [] args)throws IOException{
        File f = new File("src/org/leolo/fyp/chineseStopwordList.txt");
        System.out.println(f.getAbsolutePath());
        System.out.println(f.canRead());
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
        String line = br.readLine();
        Vector<WordList> list = new Vector<WordList>();
        while(line != null){
            if(line.length() == 0) {
                line = br.readLine();
                continue;
            }
            //System.out.println(line);
            int len = line.length();
            boolean offered = false;
            //Check is there any WordList w/ correct length
            for(int i=0;i<list.size();i++){
                if(list.get(i).getLength() == len){
                    list.get(i).offer(line);
                    offered = true;
                    break;
                }
            }
            if(!offered){
                //No list w/ correct length
                WordList wl = new WordList(len);
                wl.offer(line);
                list.add(wl);
            }
            //Postprocess
            line = br.readLine();
        }
        //All word processed
        for(int i=0;i<list.size();i++){
            String [] tmpArray = new String[0];
            System.out.println("Length="+list.get(i).getLength());
            tmpArray = list.get(i).toArray(tmpArray);
            Arrays.sort(tmpArray, String.CASE_INSENSITIVE_ORDER);
            Vector<String> dupelim = new Vector<>();
            for(int j=0;j<tmpArray.length;j++){
                if(j == 0){//Avoid ArrayIndexOutOfBoundsException
                    dupelim.add(tmpArray[j]);
                    continue;
                }
                if(!tmpArray[j-1].equals(tmpArray[j])){//Two entry are different
                    dupelim.add(tmpArray[j]);
                }
                    
            }
            tmpArray = new String[0];
            tmpArray = dupelim.toArray(tmpArray);
            for(int j=0;j<tmpArray.length;j++){
                System.out.println("\""+tmpArray[j]+"\",");
            }
        }
    }
}
