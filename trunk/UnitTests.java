import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import junit.framework.*;

public class UnitTests extends TestCase 
{
	public void testSubjectConstruction()
	{
		ReferenceMonitor rm = new ReferenceMonitor();
        SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
        
        assertTrue(hal.getSubjectName().equals("Hal"));
        assertTrue(hal.getReferenceMonitor() == rm);
        assertTrue(hal.getSecurityLevel() == SecurityLevel.HIGH);
	}
	
	public void testSleepAndRun()
	{
		ReferenceMonitor rm = new ReferenceMonitor();
		rm.createNewObject("lobj", 0);
		rm.addObjectLevel("lobj", SecurityLevel.LOW);
		rm.createNewObject("hobj", 0);
		rm.addObjectLevel("hobj", SecurityLevel.HIGH);
		SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
		rm.addSubjectLevel("Hal", SecurityLevel.HIGH);
		Date d = new Date();
		long atime = d.getTime();
		hal.start();
		while(hal.isAlive())
		{
			//waiting
		}
		d = new Date();		//refreshing time
		long btime = d.getTime(); 
		
		assertTrue(btime-atime>=500);
		
		
		/*output should be:*
		 *******************
		Hal starting up.
		
		Hal gave bad instruction.
		Current State: lobj = 0; hobj = 0
		
		Hal gave bad instruction.
		Current State: lobj = 0; hobj = 0
		
		Hal gave bad instruction.
		Current State: lobj = 0; hobj = 0
		
		Hal gave bad instruction.
		Current State: lobj = 0; hobj = 0
		
		Hal reading lobj.
		Access Granted.  Value read: 0
		Current State: lobj = 0; hobj = 0
		
		Hal writing 10 to lobj.
		Access Denied.
		Current State: lobj = 0; hobj = 0
		
		Hal gave bad instruction.
		Current State: lobj = 0; hobj = 0
		
		Hal reading hobj.
		Access Granted.  Value read: 0
		Current State: lobj = 0; hobj = 0
		
		Hal writing 999999 to hobj.
		Access Granted.  Value written: 999999
		Current State: lobj = 0; hobj = 999999
		*/
}

	public void testAdd() 
	{
        ReferenceMonitor rm = new ReferenceMonitor();
        SecureSubject hal = new SecureSubject("Hal", SecurityLevel.HIGH, "HalInstructions", rm);
        rm.addSubjectLevel("Hal", SecurityLevel.HIGH);

        assertTrue(rm.getSubjectLevel("Hal") == SecurityLevel.HIGH);

	}
	
	public void testHalRun()
	{
		
	}
} 
