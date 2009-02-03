import java.io.*

public class test {
	
	public String readFromFile(String fileName) {
	    String DataLine = "";
	    try {
	      File inFile = new File(fileName);
	      BufferedReader br = new BufferedReader(new InputStreamReader(
	          new FileInputStream(inFile)));

	      DataLine = br.readLine();
	      br.close();
	    } catch (FileNotFoundException ex) {
	      return (null);
	    } catch (IOException ex) {
	      return (null);
	    }
	    return (DataLine);

	  }

}
