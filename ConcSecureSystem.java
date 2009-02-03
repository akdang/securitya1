import java.util.HashMap;

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
    rm.tester();    
    } // main
}

class ReferenceMonitor 
{
    private ObjectManager om;

    public ReferenceMonitor()
    {
        om = new ObjectManager();
    }
    public void tester()
    {
        om.createNewObj("Bob", 0);
        System.out.println("Bob val (expecting 0):" + om.readObjVal("Bob"));
        om.setObjVal("Bob", 1);
        System.out.println("Bob val (expecting 1):" + om.readObjVal("Bob"));
        om.readObjVal("Chris");
    }
    public void addSubjectLevel(String subjName, SecurityLevel s)
    {
    }

    public void createNewObject(String objName, int objVal)
    {
    }
    

    //Todo: make this part of createObj?
    public void addObjectLevel(String objName, SecurityLevel s)
    {
    }

    private class ObjectManager
    {
        private HashMap<String,SecureObject> objects;

        private ObjectManager()
        {
            objects = new HashMap<String,SecureObject>();
        }

        private void createNewObj(String name, int val)
        {
            objects.put(name, new SecureObject(name, val));
        }

        private void setObjVal(String name, int val)
        {
            SecureObject tmp = objects.get(name);
            assert (tmp != null) : "No object mapped to key \"" + name + "\".";

            tmp.setVal(val);
        }

        private int readObjVal(String name)
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

class SecureSubject
{
    public SecureSubject(String name, SecurityLevel s, String fileName, ReferenceMonitor r)
    {
    }

    public void start()
    {
    }
}

enum SecurityLevel
{
    HIGH, LOW
}
