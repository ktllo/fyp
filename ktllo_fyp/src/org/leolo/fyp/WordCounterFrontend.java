/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.leolo.fyp;

import org.leolo.fyp.util.*;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author ktllo
 */
public class WordCounterFrontend implements WordCounterUser{
    
    public static void main(String [] args){
        try{
            new WordCounterFrontend().run();//Need access to non-static member
        }catch(Throwable t){
            t.printStackTrace();
            JOptionPane.showMessageDialog(null, t.toString());
            System.exit(1);
        }
    }
    
    private int wordCount=-1;
    
    public void run() throws Throwable{
        JFileChooser chooser = new JFileChooser();
        JOptionPane.showMessageDialog(null,"Please use UTF-8 encoding for the file contains CHINESE.");
        while(true){
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
               StringBuffer sb = new StringBuffer();
               BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile()), "utf-8"));
               String line = br.readLine();
               while(line != null){
                   sb.append(line).append('\n');
                   line = br.readLine();
               }
               br.close();
               WordCountThread wct = new WordCountThread(this,sb.toString());
               wordCount = -1;//Reset ther value
               wct.start();
               while(true){
                   if(wordCount > 0){
                       break;
                   }
                   synchronized(this){
                       this.wait();
                   }
               }
               JOptionPane.showMessageDialog(null, "Word count for "+chooser.getSelectedFile().getName()+" is "+wordCount+".");
            }else{
                break;
            }
        }
        System.exit(0);
        
    }
    
    @Override
    public void wctReport(int value) {
        wordCount = value;
    }
    
}
