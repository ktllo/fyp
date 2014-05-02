/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.io.*;
/**
 *
 * @author leolo
 */
public class FileCombiner {
    public static void main(String [] args)throws Exception{
        List<String> el = new Vector<>();
        List<String> ll = new Vector<>();
        BufferedReader br = new BufferedReader(new FileReader("vbad.txt"));
        while(true){
            String line = br.readLine();
            if(line == null) break;
            line = line.trim();
            if(line.length() == 0) continue;
            el.add(line);
        }
        br = new BufferedReader(new FileReader("bad.txt"));
        while(true){
            String line = br.readLine();
            if(line == null) break;
            line = line.trim();
            if(line.length() == 0) continue;
            ll.add(line);
        }
        br.close();
        dupelim(el);
        dupelim(ll);
        Collections.sort(el, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(ll,String.CASE_INSENSITIVE_ORDER);
        for(int i=0;i<el.size();i++){
            for(int j=0;j<ll.size();j++){
                if(el.get(i).equalsIgnoreCase(ll.get(j)))
                    ll.remove(j);
            }
        }
        
        PrintWriter out = new PrintWriter("vbad_result.txt");
        for(int i=0;i<el.size();i++)
            out.println("\""+el.get(i)+"\",");
        out.flush();
        out.close();
        out = new PrintWriter("bad_result.txt");
        for(int i=0;i<ll.size();i++)
            out.println("\""+ll.get(i)+"\",");
        out.flush();
        out.close();
        System.out.println();
    }
    
    public static void dupelim(List<String> list){
        for(int i=0;i<list.size();i++){
            for(int j=i+1;j<list.size();j++){
                if(list.get(i).equalsIgnoreCase(list.get(j))){
                    list.remove(j);
                }
            }
        }
    }
}
