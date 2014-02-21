import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.leolo.fyp.util.PostRanker;
import javax.swing.*;
/**
 *
 * @author leolo
 */
public class PostRankerDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("If you found any issue, sent me an email.");
        PostRanker pr = null;
        while(true){
            System.out.println("Keyword File. Put keywords in a text file, one keyword per line.");
            System.out.print("Path : ");
            String path = br.readLine();
            try{
                pr = new PostRanker(new File(path));
                break;
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        if(pr == null) {
            System.exit(1);
        }
        //System.setErr(null);
        while(true){
            try{
                System.out.println("Please enter the post file");
                System.out.println("Enter \"quit\" to exit");
                System.out.print("Path : ");
                String path = br.readLine();
                if(path.equalsIgnoreCase("quit")){
                    break;
                }
                File f = new File(path);
                if(f.isFile()){
                    System.out.print("Point got : ");
                    System.out.println(pr.rank(f));
                }else{
                    File [] fs = f.listFiles();
                    for(int i=0;i<fs.length;i++){
                        if(fs[i].isFile()){
                            try{
                                System.out.println("Point got for "+fs[i].getName() + " : "+pr.rank(fs[i]));
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        System.out.println("Thank you!");
    }
}
