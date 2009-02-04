import java.util.*;
import java.io.*;

class ConcSecureSystem {

    public static void main (String args[]) 
    {
	ReferenceMonitor rm = new ReferenceMonitor();
	
	// Create a high level subject Hal getting instructions from the file "HalInstructions"
	SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
	rm.addSubjectLevel("Hal", SecurityLevel.HIGH);

	// Create a low level subject Lyle getting instructions from the file "LyleInstructions"
	SecureSubject lyle = new SecureSubject("Lyle", SecurityLevel.LOW, "LyleInstructions", rm);
	rm.addSubjectLevel("Lyle", SecurityLevel.LOW);

	// Create a high level object lobj with initial value 0
	rm.createNewObject("hobj", 0);
	rm.addObjectLevel("hobj", SecurityLevel.HIGH);

	// Create a low level object lobj with initial value 0
	rm.createNewObject("lobj", 0);
	rm.addObjectLevel("lobj", SecurityLevel.LOW);

    lyle.start();
	hal.start();

    } // main
}

class ReferenceMonitor 
{
    private ObjectManager om;
    private HashMap<String, SecurityLevel> objectLevels;
    private HashMap<String, SecurityLevel> subjectLevels;

    public ReferenceMonitor()
    {
        om = new ObjectManager();
        objectLevels = new HashMap<String, SecurityLevel>();
        subjectLevels = new HashMap<String, SecurityLevel>();
    }

    public synchronized int executeInstruction(String name, Instruction instr)
    {
        SecurityLevel subjectLevel = subjectLevels.get(name);
        SecurityLevel objectLevel = objectLevels.get(instr.getObjName());
        int value = -1; //default for bad instruction
        
        System.out.println();

        switch(instr.getCommand())
        {
            case READ:
                System.out.println(name + " reads " + instr.getObjName() + ".");
                if(subjectLevel.compareTo(objectLevel) >= 0)
                {
                    value = om.readObjectVal(instr.getObjName());
                    System.out.println("Access Granted.  Value read: " + value);
                }
                else
                   System.out.println("Access Denied.");
                break;

            case WRITE:
                System.out.println(name + " writes " + instr.getValue() + " to " + instr.getObjName() + ".");
                if(subjectLevel.compareTo(objectLevel) <= 0)
                {
                    om.setObjectVal(instr.getObjName(), instr.getValue());
                    value = om.readObjectVal(instr.getObjName());
                    System.out.println("Access Granted.  Value written: " + value);
                }
                else
                   System.out.println("Access Denied.");
                break;
        
            case SLEEP: System.out.println(name + " sleeping.  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%GETOUTAHERE%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                break;

            default: System.out.println(name + " gave bad instruction.");
                break;
        }

        System.out.println("Current State: lobj = " + om.readObjectVal("lobj") + "; hobj = " + om.readObjectVal("hobj"));

        return value;

    }
    
    public void addSubjectLevel(String name, SecurityLevel s)
    {
        subjectLevels.put(name, s);
    }

    public SecurityLevel getSubjectLevel(String name)
    {
        assert subjectLevels.get(name) != null : "No object mapped to key \"" + name + "\".";
        return subjectLevels.get(name);
    }
    
    public void addObjectLevel(String name, SecurityLevel s)
    {
        objectLevels.put(name, s);
    }

    public void createNewObject(String name, int val)
    {
        om.createNewObject(name, val);
    }
    
    
    private class ObjectManager
    {
        private HashMap<String,SecureObject> objects;

        private ObjectManager()
        {
            objects = new HashMap<String,SecureObject>();
        }

        private void createNewObject(String name, int val)
        {
            objects.put(name, new SecureObject(name, val));
        }

        private void setObjectVal(String name, int val)
        {
            SecureObject tmp = objects.get(name);
            assert (tmp != null) : "No object mapped to key \"" + name + "\".";

            tmp.setVal(val);
        }

        private int readObjectVal(String name)
        {
            SecureObject tmp = objects.get(name);
            assert (tmp != null) : "No object mapped to key \"" + name + "\".";
            
            return tmp.getVal();
        }

        private class SecureObject
        {
            private String name;
            private int val;

            private SecureObject(String name, int val)
            {
                this.name = name;
                this.val = val;
            }

            private String getName()
            {
                return name;
            }

            private int getVal()
            {
                return val;
            }

            private void setVal(int val)
            {
                this.val = val;
            }
        }
    }

}

class SecureSubject extends Thread
{
    private String name;
    private SecurityLevel securityLevel;
    private ReferenceMonitor referenceMonitor;
    private ArrayList<Instruction> instructions;

    public SecureSubject(String name, SecurityLevel s, String fileName, ReferenceMonitor r) 
    {
        this.name = name;
        this.securityLevel = s;
        this.referenceMonitor = r;
        instructions = new ArrayList<Instruction>();

        Scanner in = null;
	    try 
        {
	        in = new Scanner(new File(fileName));
	        in.useDelimiter("\n");

	        while (in.hasNext()) 
            {
	            String nextInstruction = in.next().toLowerCase().trim();
	            
                if(nextInstruction.isEmpty())
                    continue;

	            StringTokenizer nextInstructionTokens = new StringTokenizer(nextInstruction, " ", false);
	            ArrayList<String> tokensToBeParsed = new ArrayList<String>();
	            
	            while(nextInstructionTokens.hasMoreTokens()) 
                {
	            	String nextToken = nextInstructionTokens.nextToken();
	            	tokensToBeParsed.add(nextToken);
	            }
	            instructions.add(Instruction.parseInstruction(tokensToBeParsed));
	        }
	    } 
        catch (FileNotFoundException e) 
        {
			System.err.println("File Not Found!");
            System.exit(1);
		}
        finally 
        {
            if(in!=null)
    	        in.close();
	    }
    }

    public void run()
    {
        System.out.println(name + " starting up.");

        for(Instruction i: instructions)
        {
            int value = referenceMonitor.executeInstruction(name, i);
        }
    }
}

enum SecurityLevel {LOW, HIGH}

enum Command {WRITE, READ, SLEEP, BADINSTRUCTION}

class Instruction {	
	public static final String WRITE = "write";
	public static final String READ = "read";
	public static final String SLEEP = "sleep";
	public static final String LOBJ = "lobj";
	public static final String HOBJ = "hobj";
	
	private Command command;
    private	String obj;
    private	int value;
	
	Instruction(Command command, String obj, int value)
    {
		this.command = command;
		this.obj = obj;
		this.value = value;
	}
	
	public Command getCommand() 
    {
		return command;
	}
	
	public String getObjName() 
    {
		return obj;
	}
	
	public int getValue() 
    {
		return value;
	}
	
	public static Instruction parseInstruction(ArrayList<String> tokensToBeParsed) 
    {
	    	
		int size = tokensToBeParsed.size();
		
        if(size == 1)       //SLEEP
        {
            if(tokensToBeParsed.get(0).equals(SLEEP))
                return new Instruction(Command.SLEEP, "" ,  -1);
        }
		else if(size == 2)  //READ
        {
            if(tokensToBeParsed.get(0).equals(READ))
                if(tokensToBeParsed.get(1).equals(LOBJ) || tokensToBeParsed.get(1).equals(HOBJ))
                    return new Instruction(Command.READ, tokensToBeParsed.get(1),  -1);
		}
		else if(size == 3)  //WRITE
        {
			if(tokensToBeParsed.get(0).equals(WRITE))
            {
                if(tokensToBeParsed.get(1).equals(LOBJ) || tokensToBeParsed.get(1).equals(HOBJ))
                {
                    try
                    {
                        int val = Integer.parseInt(tokensToBeParsed.get(2));
                        return new Instruction(Command.WRITE, tokensToBeParsed.get(1),  val);
                    }
                    catch (NumberFormatException e)
                    {
                        return new Instruction(Command.BADINSTRUCTION, "", -1);
                    }

                }
            }
		}
        return new Instruction(Command.BADINSTRUCTION, "", -1);
    }
}
