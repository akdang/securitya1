import junit.framework.*;

public class UnitTests extends TestCase 
{
	public void testSubjectConstruction()
	{
		ReferenceMonitor rm = new ReferenceMonitor();
        SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);

        //assertTrue(hal.getName().equals("));

	}
	public void testAdd() 
	{
        ReferenceMonitor rm = new ReferenceMonitor();
        SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
        rm.addSubjectLevel("Hal", SecurityLevel.HIGH);

        assertTrue(rm.getSubjectLevel("Hal") == SecurityLevel.HIGH);

	}
} 
