
import java.util.Arrays;
import org.leolo.fyp.KeywordList;

public class Tester {
	public static void main(String [] args){
		Arrays.sort(KeywordList.ENGLISH,String.CASE_INSENSITIVE_ORDER);
		for(int i=0;i<KeywordList.ENGLISH.length;i++){
			System.out.println("\""+KeywordList.ENGLISH[i]+"\",");
		}
		System.out.println();
		
		System.out.println(Arrays.binarySearch(KeywordList.ENGLISH, "HongKong", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.ENGLISH, "ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.ENGLISH, "Ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.ENGLISH, "a", String.CASE_INSENSITIVE_ORDER));
		
		
		for(int i=0;i<KeywordList.CHINESE.length;i++) {
                    System.out.println(KeywordList.CHINESE[i]);
                }
		Arrays.sort(KeywordList.CHINESE,String.CASE_INSENSITIVE_ORDER);
		System.out.println("*****************************");
		for(int i=0;i<KeywordList.CHINESE.length;i++) {
                    System.out.println(KeywordList.CHINESE[i]);
                }
		System.out.println(Arrays.binarySearch(KeywordList.CHINESE, "�����u", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.CHINESE, "���u��", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.CHINESE, "�|�P", String.CASE_INSENSITIVE_ORDER));
		System.out.println("*****************************");
		System.out.println(Arrays.binarySearch(KeywordList.CHINESE, "ab", String.CASE_INSENSITIVE_ORDER));
		System.out.println(Arrays.binarySearch(KeywordList.ENGLISH, "�|�P", String.CASE_INSENSITIVE_ORDER));
		
	}
}
