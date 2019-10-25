package a2;

import static org.junit.Assert.*;

import org.junit.Test;

public class InspectorTest {


	@Test
	public void testGetValueByString() {
		Inspector i = new Inspector();
		Integer in = new Integer("5");
		
		String test = i.getValueByString(in);
		assertEquals(test, "5");
	}

	@Test
	public void testPrintObjectArray() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testInspectMethod()
	{
		try
		{	
			
			Inspector i = new Inspector();
			
			Object a = new ClassA(15);
			Object b = new ClassB();
			Object b2 = new ClassB();
			Object d = new ClassD();
			Object d2 = new ClassD();
			
			Object [] arrayA = new ClassA[10];
			int testInt = 123; 
			
			// Should be false (we haven't inspected anything yet!)
			assertEquals(i.hasBeenInspected(a), false);
			assertEquals(i.hasBeenInspected(b), false);
			assertEquals(i.hasBeenInspected(d), false);
			
			
			// Inspect objects
			i.inspect(a, false);
			i.inspect(b, false);
			i.inspect(d, false);
			
			//Inspected three objects so should have three object codes
			//assertEquals(i.getObjectCodes().size(), 3);
			
			// Now first three objects has been inspected
			// Should be false (we haven't inspected anything yet!)
			assertEquals(i.hasBeenInspected(a), true);
			assertEquals(i.hasBeenInspected(b), true);
			assertEquals(i.hasBeenInspected(d), true);
			
			assertEquals(i.hasBeenInspected(d2), false);
			// Same class but different instance with recursion
			
			
			// We should have 3 object codes at this stage 
			assertEquals(i.getObjectCodes().size(), 3);
			
			
			// Now test recursive cases
			i.inspect(a, true);
			assertEquals(i.getObjectCodes().size(), 3);
			i.inspect(b2, true);
			
			// Nothing from a plus 3 object codes from b2 which should yield 6
			assertEquals(i.getObjectCodes().size(), 6);
	
			i.inspect(d2, true);
		
			// Class D has 2 object codes so now we should have 8
			assertEquals(i.getObjectCodes().size(), 8);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCheckFields()
	{
		fail("Not yet implemented");
	}
	
	

}
