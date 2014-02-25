/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.leolo.fyp.util.StopwordRemoverThread;
/**
 *
 * @author leolo
 */
public class SWRTTester {
    public static void main(String [] args){
        String testString="This is a test-message. Is it a good idea. You couldn\'t disagree with me.";
        StopwordRemoverThread swrt = new StopwordRemoverThread(null,testString);
        System.out.println(testString);
        swrt.removeStopword();
        System.out.println(swrt.getString());
        
    }
}
