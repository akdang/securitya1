import junit.framework.*;

public class UnitTests extends TestCase 
{
	public void testAdd() 
	{
        ReferenceMonitor rm = new ReferenceMonitor();
        SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
        rm.addSubjectLevel("Hal", SecurityLevel.HIGH);

        assertTrue(rm.getSubjectLevel("Hal") == SecurityLevel.HIGH);

	}
} 
