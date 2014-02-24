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
        String testString="The term void ab inito is means that it become invaild from the begining. It is a special term about law. Although it is a special terms, many people are able to use the term void ab inito, Yes? ZZZZ";
        StopwordRemoverThread swrt = new StopwordRemoverThread(null,testString);
        System.out.println(testString);
        swrt.removeStopword();
        System.out.println(swrt.getString());
        
    }
}
