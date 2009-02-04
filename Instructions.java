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
	            String nextInstruction = in.useDelimiter("\n").next().toLowerCase().trim();
	            
	            StringTokenizer nextInstructionTokens = new StringTokenizer(nextInstruction, " ", false);
	            ArrayList<String> tokensToBeParsed = new ArrayList<String>();
	            
	            while(nextInstructionTokens.hasMoreTokens()) {
	            	String nextToken = nextInstructionTokens.nextToken();
	            	tokensToBeParsed.add(nextToken);
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
		String command = tokensToBeParsed.get(0);
		String obj = "";
		int value = -1; 
		
		if(size >= 2) {
			obj = tokensToBeParsed.get(1);
		}
		else if(size >= 3) {
			value = Integer.parseInt(tokensToBeParsed.get(2));
		}
		else {
			System.out.println("badinstruction");
			return new Instructions(commands.BADINSTRUCTION, "", -1);
		}
			
		if(command.contains(SLEEP)) {
			Instructions instruction = new Instructions(commands.SLEEP, "", -1);
			System.out.println(instruction.getCommand());
			return instruction;
		}
		
		else if(command.contains(READ)) {
			if(obj.contains(LOBJ)) {
				Instructions instruction = new Instructions(commands.READ, LOBJ, -1);
				System.out.println(instruction.getCommand() + " " + instruction.getObj());
				return instruction;
			}
			else if(obj.contains(HOBJ)) {
				Instructions instruction = new Instructions(commands.READ, HOBJ, -1);
				System.out.println(instruction.getCommand() + " " + instruction.getObj());
				return instruction;
			}
		}
		
		else if(command.contains(WRITE)) {
			if(obj.contains(LOBJ)) {
				Instructions instruction = new Instructions(commands.WRITE, LOBJ, value);
				System.out.println(instruction.getCommand() + " " + instruction.getObj() + " " + instruction.getValue());
				return instruction;
			}
		}
	
		return new Instructions(commands.BADINSTRUCTION, "", -1);
		
	}
}

//		
//		// test for READ/WRITE/SLEEP commands
//		if(!nextInstruction.contains(WRITE) && 
//				!nextInstruction.contains(READ) && 
//				!nextInstruction.contains(SLEEP))
//			return false;
//		
//		
//		// test for valid requested object
//		if(!nextInstruction.contains(LOBJ) && 
//				!nextInstruction.contains(HOBJ))
//			return false;
//		
//		// test for correct number of arguments
//		if (nextInstruction.contains(LOBJ)) {
//			
//			if(nextInstruction.contains(READ)) {
//				String[] lobjSplit = nextInstruction.split(LOBJ);
//				for(String nextToken : lobjSplit) {
//					System.out.println(nextToken.trim());
//					if(!nextToken.trim().isEmpty() && !nextToken.trim().contains(READ))
//						return false;
//				}
//			}
//			
//			else {
//				String[] lobjSplit = nextInstruction.split(LOBJ);
//				for(String nextToken : lobjSplit) {
//					System.out.println(nextToken.trim());
//					if(!nextToken.trim().isEmpty() && !nextToken.trim().contains(WRITE))
//						return false;
//				}
//			}
//				
//		}
//		
//		else if (nextInstruction.contains(HOBJ)){private
//			
//			if(nextInstruction.contains(READ)) {
//				String[] lobjSplit = nextInstruction.split(HOBJ);
//				for(String nextToken : lobjSplit) {
//					System.out.println(nextToken.trim());
//					if(!nextToken.trim().isEmpty() && !nextToken.trim().contains(READ))
//						return false;
//				}
//			}
//			
//			else {
//				String[] lobjSplit = nextInstruction.split(HOBJ);
//				for(String nextToken : lobjSplit) {
//					System.out.println(nextToken.trim());
//					if(!nextToken.trim().isEmpty() && !nextToken.trim().contains(WRITE))
//						return false;
//				}//	}
	//}
//			}
//		}
//
//		
//		return true;
//	}
//}





