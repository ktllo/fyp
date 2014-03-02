/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.leolo.fyp.util.StopwordRemoverThread;

import java.io.*;
/**
 *
 * @author leolo
 */
public class SWRTTester {
    public static void main(String [] args) throws Exception{
        String inputDirectory = "C:\\Users\\leolo\\Dropbox\\FYP\\EnglishOnly";
        String outputDirectory= "C:\\Users\\leolo\\Dropbox\\FYP\\EnglishOnlyOutput";
        File inDir = new File(inputDirectory);
        File [] target = inDir.listFiles();//List all file in the directory
        for(int i=0;i<target.length;i++){
            if(target[i].isFile() && target[i].canRead()){//Only file that readable will be processed.
                //Read File
                System.err.println("Trying "+target[i].getAbsolutePath());
                StringBuffer data = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(target[i]), "utf-8"));
                String line = br.readLine();
                while(line != null){
                    data.append(line).append('\n');
                    line = br.readLine();
                }
                br.close();
                StopwordRemoverThread swrt = new StopwordRemoverThread(null,data.toString());
                swrt.removeStopword();
                String result = swrt.getString();
                PrintWriter out = new PrintWriter(new FileWriter(outputDirectory+"/"+target[i].getName()));
                out.print(result);
                out.flush();
                out.close();
                System.err.println(result);
                System.err.println("Finishing "+target[i].getAbsolutePath());
            }
        }
    }
}
