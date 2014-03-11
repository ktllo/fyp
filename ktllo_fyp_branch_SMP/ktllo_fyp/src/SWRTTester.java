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
        String datain="中國遭如上所述到電視機爆炸";
        StopwordRemoverThread t = new StopwordRemoverThread(null,datain);
        t.removeStopword();
        System.out.println(t.getString());
    }
}
