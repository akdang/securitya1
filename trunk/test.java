import java.util.ArrayList;
import java.util.StringTokenizer;


public class test {
	
	public static void main(String []args) {
		ArrayList<String> write = new ArrayList<String>();
		
		int i = 0;
		String s = "write      lobj           2323 3232        ";
		StringTokenizer test = new StringTokenizer(s, " ", false);
		while(i <= test.countTokens()) {
			write.add(test.nextToken());
			
			i++;
		}
		
		System.out.println(write);
		
		
		
		
		
		
		
//		String[] split = s.split("lobj");
//		
//		for(String token : split) {
//			System.out.println("here: " + token);
//			System.out.println(!token.isEmpty() && !token.contains("read"));
//		}
	}
}
		

