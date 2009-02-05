import java.util.*;
import java.io.*;

enum SecurityLevel {LOW, HIGH}

enum Command {WRITE, READ, SLEEP, BADINSTRUCTION}

/**
 * Class implements Bell-LaPadula model.
 */
public class ConcSecureSystem 
{
	/**
	 * Entry point for program execution.
	 * @param args
	 */
    public static void main (String args[]) 
    {
		ReferenceMonitor rm = new ReferenceMonitor();
		
		SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
		rm.addSubjectLevel("Hal", SecurityLevel.HIGH);
	
		SecureSubject lyle = new SecureSubject("Lyle", SecurityLevel.LOW, "LyleInstructions", rm);
		rm.addSubjectLevel("Lyle", SecurityLevel.LOW);
	
		rm.createNewObject("hobj", 0);
		rm.addObjectLevel("hobj", SecurityLevel.HIGH);
	
		rm.createNewObject("lobj", 0);
		rm.addObjectLevel("lobj", SecurityLevel.LOW);
	
	    //lyle.start();
		hal.start();
    }
}

/**
 * RefernceMonitor controls all subject access to objects.
 */
class ReferenceMonitor 
{
	/* Each instance of ReferenceMonitor has its own object manager. 
	 * This design ensures that the objects cannot be accessed by going around the ReferenceMonitor */
    private ObjectManager om;
    
    //hashes containing security levels for objects and subjects
    private HashMap<String, SecurityLevel> objectLevels;
    private HashMap<String, SecurityLevel> subjectLevels;

    /**
     * Constructs instance of ReferenceMonitor.
     */
    public ReferenceMonitor()
    {
        om = new ObjectManager();
        objectLevels = new HashMap<String, SecurityLevel>();
        subjectLevels = new HashMap<String, SecurityLevel>();
    }

    /**
     * Executes instr only if the security levels of the subject and object in question
     * are such that the operation is permitted under the rules of Simple Security (reading) and 
     * the *-property (writing). 
     * @param name describes subject executing instruction
     * @param instr instruction subject wishes to execute
     * @return 0 if bad instruction given, read access denied, or write performed
     * @return value read if read access granted
     */
    public synchronized int executeInstruction(String name, Instruction instr)
    {
    	assert (instr.getCommand() != Command.SLEEP) : "Sleep instruction passed to reference monitor!";
        SecurityLevel subjectLevel = subjectLevels.get(name);
        SecurityLevel objectLevel = objectLevels.get(instr.getObjectName());
        int value = 0; //default for bad instruction
        
        System.out.println();

        switch(instr.getCommand())
        {
            case READ:
                System.out.println(name + " reading " + instr.getObjectName() + ".");
                if(subjectLevel.compareTo(objectLevel) >= 0)
                {
                    value = om.getObjectValue(instr.getObjectName());
                    System.out.println("Access Granted.  Value read: " + value);
                }
                else
                   System.out.println("Access Denied.");
                break;

            case WRITE:
                System.out.println(name + " writing " + instr.getValue() + " to " + instr.getObjectName() + ".");
                if(subjectLevel.compareTo(objectLevel) <= 0)
                {
                    om.setObjectValue(instr.getObjectName(), instr.getValue());
                    value = om.getObjectValue(instr.getObjectName());
                    System.out.println("Access Granted.  Value written: " + value);
                }
                else
                   System.out.println("Access Denied.");
                break;

            default: System.out.println(name + " gave bad instruction.");
                break;
        }

        System.out.println("Current State: lobj = " + om.getObjectValue("lobj") + "; hobj = " + om.getObjectValue("hobj"));

        return value;
    }
    
    /**
     * Add subject name and security level to be maintained by ReferenceMonitor.
     * @param name subject name
     * @param s subject security level
     */
    public void addSubjectLevel(String name, SecurityLevel s)
    {
        subjectLevels.put(name, s);
    }
    
    /**
     * Get security level for subject.
     * @precondition name provide must be valid name of subject known to reference monitor
     * @param name subjects name
     * @return SecurityLevel of subject
     */
    public SecurityLevel getSubjectLevel(String name)
    {
        assert subjectLevels.get(name) != null : "No subject mapped to key \"" + name + "\".";
        return subjectLevels.get(name);
    }
    
    /**
     * Add object name and security level to be maintained by ReferenceMonitor.
     * @param name object name
     * @param s object security level
     */
    public void addObjectLevel(String name, SecurityLevel s)
    {
        objectLevels.put(name.toLowerCase(), s);
    }

    /**
     * Get security level for object.
     * @precondition name provided must be valid name of object known to reference monitor
     * @param name object's name
     * @return security level of object
     */
    public SecurityLevel getObjectLevel(String name)
    {
    	name = name.toLowerCase();
    	assert objectLevels.get(name) != null : "No object mapped to key \"" + name + "\".";
    	return objectLevels.get(name);
    }

    /**
     * Instructs underlying object manager to create a new object. 
     * Object names are not case sensitive and are set to all lower case.
     * @param name object's name
     * @param value object's value
     */
    public void createNewObject(String name, int value)
    {
        om.createNewObject(name, value);
    }


    
    /**
     * Class manages creation, modification and reading of objects.
     */
    private class ObjectManager
    {
    	//Map of objects managed
        private HashMap<String,SecureObject> objects;

        /**
         * Default constructor.
         */
        private ObjectManager()
        {
            objects = new HashMap<String,SecureObject>();
        }
        
        /**
         * Creates new object to be managed by manager.
         * @param name object's name
         * @param value object's value
         */
        private void createNewObject(String name, int value)
        {
            objects.put(name, new SecureObject(name, value));
        }

        /**
         * Sets value of described object
         * @param name object's name
         * @param value to be set
         */
        private void setObjectValue(String name, int value)
        {
            SecureObject tmp = objects.get(name);
            assert (tmp != null) : "No object mapped to key \"" + name + "\".";

            tmp.setVal(value);
        }

        /**
         * Gets value of given object.
         * @param name object's name
         * @return value of object
         */
        private int getObjectValue(String name)
        {
            SecureObject tmp = objects.get(name);
            assert (tmp != null) : "No object mapped to key \"" + name + "\".";
            
            return tmp.getValue();
        }

        /**
         * Class defines objects.
         */
        private class SecureObject
        {
            private String name;
            private int value;

            /**
             * Constructor
             * @param name object's name
             * @param value object's value
             */
            private SecureObject(String name, int value)
            {
                this.name = name;
                this.value = value;
            }

            /**
             * @return object name
             */
			private String getName()
            {
                return name;
            }

			/**
			 * @return object value
			 */
            private int getValue()
            {
                return value;
            }

            /**
             * Set this object's value
             * @param value new value to be set
             */
            private void setVal(int value)
            {
                this.value = value;
            }
        }//SecureObject
    }//ObjectManager
}//ReferenceMonitor

/*
 * Defines subjects.
 */
class SecureSubject extends Thread
{
    private String name;
    private SecurityLevel securityLevel;
    private ReferenceMonitor referenceMonitor;
    private ArrayList<Instruction> instructions;

    /**
     * Constructor. Reads in file to determine subject instructions
     * @param name subject name
     * @param s subject security level
     * @param fileName name of file containing instructions
     * @param r reference monitor controlling subject access to objects
     */
    public SecureSubject(String name, SecurityLevel s, String fileName, ReferenceMonitor r) 
    {
        this.name = name;
        securityLevel = s;
        referenceMonitor = r;
        instructions = new ArrayList<Instruction>();

        Scanner in = null;
	    try 
        {
	        in = new Scanner(new File(fileName));
	        in.useDelimiter("\n");

	        while (in.hasNext()) 
            {
	            String nextInstruction = in.next().toLowerCase().trim();	//ignoring case and edge white space
	            
	            StringTokenizer nextInstructionTokens = new StringTokenizer(nextInstruction, " ", false);	//single instruction into parts
	            ArrayList<String> tokensToBeParsed = new ArrayList<String>();
	            
	            while(nextInstructionTokens.hasMoreTokens())	//populating ArrayList with tokens
                {
	            	String nextToken = nextInstructionTokens.nextToken();
	            	tokensToBeParsed.add(nextToken);
	            }
	            instructions.add(Instruction.parseInstruction(tokensToBeParsed));	//parsing instruction, adding to list of subject instructions
	        }
	    } 
        catch (FileNotFoundException e) 
        {
			System.err.println("File " + fileName + " not found! Exiting.");
            System.exit(1);
		}
        finally 
        {
            if(in!=null)
    	        in.close();
	    }
    }
    
    /**
     * @return subject's name
     */
    public String getSubjectName() 
    {
		return name;
	}

    /**
     * @return subject's security level
     */
	public SecurityLevel getSecurityLevel() 
	{
		return securityLevel;
	}

	/**
	 * @return subject's reference monitor
	 */
	public ReferenceMonitor getReferenceMonitor() 
	{
		return referenceMonitor;
	}

	/**
	 * Invoked when subject thread's start() method executed.
	 * Attempts to execute instructions in file, passing them to the reference monitor.
	 */
	public void run()
    {
        System.out.println(name + " starting up.");

        for(Instruction i: instructions)
        {
        	if(i.getCommand() == Command.SLEEP)
        	{
				try {
					Thread.sleep(500);	//current thread sleeps .5 seconds
				} catch (InterruptedException e) {					
					//do nothing, interruption of sleep is of no concern for these threads
				}
			}
        	else
        	{
        		int value = referenceMonitor.executeInstruction(name, i);
        	}
        }
    }
}

class Instruction 
{	
	public static final String WRITE = "write";
	public static final String READ = "read";
	public static final String SLEEP = "sleep";
	public static final String LOBJ = "lobj";
	public static final String HOBJ = "hobj";
	
	private Command command;
    private	String objName;
    private	int value;
	
	Instruction(Command command, String objName, int value)
    {
		this.command = command;
		this.objName = objName;
		this.value = value;
	}
	
	public Command getCommand() 
    {
		return command;
	}
	
	public String getObjectName() 
    {
		return objName;
	}
	
	public int getValue() 
    {
		return value;
	}
	
	public static Instruction parseInstruction(ArrayList<String> tokensToBeParsed) 
    {
		int size = tokensToBeParsed.size();
		
        if(size == 1 && tokensToBeParsed.get(0).equals(SLEEP))		//SLEEP
        {
            return new Instruction(Command.SLEEP, "" ,  -1);
        }
		else if(size == 2 && tokensToBeParsed.get(0).equals(READ))  //READ
        {
            if(tokensToBeParsed.get(1).equals(LOBJ) || tokensToBeParsed.get(1).equals(HOBJ))
                return new Instruction(Command.READ, tokensToBeParsed.get(1),  -1);
		}
		else if(size == 3 && tokensToBeParsed.get(0).equals(WRITE))	//WRITE
        {
            if(tokensToBeParsed.get(1).equals(LOBJ) || tokensToBeParsed.get(1).equals(HOBJ))
            {
                try
                {
                    int value = Integer.parseInt(tokensToBeParsed.get(2));
                    return new Instruction(Command.WRITE, tokensToBeParsed.get(1),  value);
                }
                catch (NumberFormatException e)
                {
                    return new Instruction(Command.BADINSTRUCTION, "", -1);
                }
            }
		}
        return new Instruction(Command.BADINSTRUCTION, "", -1);
    }
}
