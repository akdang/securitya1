import java.util.*;

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

    rm.printer();
    } // main
}

class ReferenceMonitor 
{
    private ObjectManager om;
    private HashMap<String, SecurityLevel> objectLevels;
    private HashMap<String, SecurityLevel> subjectLevels;

    //delete me
    public void printer()
    {
        Iterator<String> it = objectLevels.keySet().iterator();
        while(it.hasNext())
        {
            String tmp = it.next();
            System.out.println("obj: " + tmp + "\tlvl: " + objectLevels.get(tmp));
        }

        it = subjectLevels.keySet().iterator();
        while(it.hasNext())
        {
            String tmp = it.next();
            System.out.println("subj: " + tmp + "\tlvl: " + subjectLevels.get(tmp));
        }
    }

    public ReferenceMonitor()
    {
        om = new ObjectManager();
        objectLevels = new HashMap<String, SecurityLevel>();
        subjectLevels = new HashMap<String, SecurityLevel>();
    }

    public synchronized void executeInstruction(String name, Instruction i)
    {
        System.out.println("Executing instruction " + i + " for " + name + ".");
    }
    
    public void addSubjectLevel(String name, SecurityLevel s)
    {
        subjectLevels.put(name, s);
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
        this.instructions = new ArrayList<Instruction>();
    }

    public void run()
    {
        System.out.println(name + " starting up.");

        for(Instruction i: instructions)
        {
            referenceMonitor.executeInstruction(name, i);
        }
    }
}

class Instruction {
    String s;
    public String toString() { return s; }
    public Instruction()
    {
        this.s = "blank";
    }

    public Instruction(String s)
    { this.s = s;
    }
}

enum SecurityLevel
{
    HIGH, LOW
}
