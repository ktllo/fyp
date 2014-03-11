
import java.util.Arrays;
import org.leolo.fyp.StopwordList;

public class Tester {
	public static void main(String [] args){
		Arrays.sort(StopwordList.ENGLISH,String.CASE_INSENSITIVE_ORDER);
		for(int i=0;i<StopwordList.ENGLISH.length;i++){
			System.out.println("\""+StopwordList.ENGLISH[i]+"\",");
		}
		System.out.println();
		
		System.out.println(Arrays.binarySearch(StopwordList.ENGLISH, "HongKong", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ENGLISH, "ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ENGLISH, "Ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ENGLISH, "a", String.CASE_INSENSITIVE_ORDER));
		
		
		for(int i=0;i<StopwordList.ANOTHER.length;i++) {
                    System.out.println(StopwordList.ANOTHER[i]);
                }
		Arrays.sort(StopwordList.ANOTHER,String.CASE_INSENSITIVE_ORDER);
		System.out.println("*****************************");
		for(int i=0;i<StopwordList.ANOTHER.length;i++) {
                    System.out.println(StopwordList.ANOTHER[i]);
                }
		System.out.println(Arrays.binarySearch(StopwordList.ANOTHER, "片相", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ANOTHER, "相片", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ANOTHER, "片", String.CASE_INSENSITIVE_ORDER));
		System.out.println("*****************************");
		System.out.println(Arrays.binarySearch(StopwordList.ANOTHER, "ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(StopwordList.ENGLISH, "相片", String.CASE_INSENSITIVE_ORDER));
		
	}
}
