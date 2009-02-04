import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

enum commands {WRITE, READ, SLEEP, BADINSTRUCTION}

public class Instructions {	
	public static final String WRITE = "write";
	public static final String READ = "read";
	public static final String SLEEP = "sleep";
	public static final String LOBJ = "lobj";
	public static final String HOBJ = "hobj";
	
	commands command;
	String obj;
	int value;
	
	Instructions(commands command, String obj, int value){
		this.command = command;
		this.obj = obj;
		this.value = value;
	}
	
	public commands getCommand() {
		return command;
	}
	
	public String getObj() {
		return obj;
	}
	
	public int getValue() {
		return value;
	}
	
	public static void main(String[] args) {
		
	    Scanner in = null;
	    try {
	        in = new Scanner(new File("HalInstructions"));
	
	        while (in.hasNext()) {
	            String nextInstruction = in.useDelimiter("\n").next().toLowerCase();
	            
	            StringTokenizer nextInstructionTokens = new StringTokenizer(nextInstruction, " ", false);
	            ArrayList<String> tokensToBeParsed = new ArrayList<String>();
	            
	            while(nextInstructionTokens.hasMoreTokens()) {
	            	String nextToken = nextInstructionTokens.nextToken();
	            	tokensToBeParsed.add(nextToken);
	            	System.out.println(nextToken);
	            }
	            
	            parseInstruction(tokensToBeParsed);
	        }
	        
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (in != null) {
	            in.close();
	        }
	    }
	}
	
	public static Instructions parseInstruction(ArrayList<String> tokensToBeParsed) {
		int size = tokensToBeParsed.size();
		
		String command = "";
		String obj = "";
		int value = -1; 
		
		if (size >= 1) {
			 command = tokensToBeParsed.get(0);
		}
		if(size >= 2) {
			obj = tokensToBeParsed.get(1);
		}
		if(size >= 3) {
			value = Integer.parseInt(tokensToBeParsed.get(2));
		}
		if(size >= 4) {
			System.out.println("badinstruction");
			return new Instructions(commands.BADINSTRUCTION, "", -1);
		}
			
		if(command.contains(SLEEP)) {
			Instructions instruction = new Instructions(commands.SLEEP, "", -1);
			System.out.println(instruction.getCommand());
			return instruction;
		}
		
		if(command.contains(READ) && size <= 2) {
			if(obj.equals(LOBJ) || obj.equals(HOBJ)) {
				Instructions instruction = new Instructions(commands.READ, obj, -1);
				System.out.println(instruction.getCommand() + " " + instruction.getObj());
				return instruction;
			}
		}
		else {
			System.out.println("badinstruction");
			return new Instructions(commands.BADINSTRUCTION, "", -1);
		}
		
		if(command.contains(WRITE) && size <= 3) {
			if(obj.equals(LOBJ) || obj.equals(HOBJ)) {
				Instructions instruction = new Instructions(commands.WRITE, obj, value);
				System.out.println(instruction.getCommand() + " " + instruction.getObj() + " " + instruction.getValue());
				return instruction;
			}
		}
		else {
			System.out.println("badinstruction");
			return new Instructions(commands.BADINSTRUCTION, "", -1);
		}
			
		System.out.println("badinstruction");
		return new Instructions(commands.BADINSTRUCTION, "", -1);
		
	}
}






