package a2;

import static org.junit.Assert.*;

import org.junit.Test;

public class InspectorTest {


	@Test
	public void testGetValueByString() {
		Inspector i = new Inspector();
		
		// Test with integer wrapper class 
		Integer in = new Integer("5");
		
		String test = i.getValueByString(in);
		assertEquals(test, "5");
		
		
		// Test with short wrapper class 
		Short sh = new Short((short) 3);
		assertEquals( "3", i.getValueByString(sh));
		
		// Now with boolean wrapper class 
		Boolean b = new Boolean(true);
		assertEquals("true", i.getValueByString(b));
	}

	
	@Test
	public void testHasBeenInspected()
	{
		Inspector i = new Inspector();
		
		try {
			
			Object a = new ClassA(15);
			Object b = new ClassB();
			Object d = new ClassD();
		
			
			// hasn't yet been inspected 
			assertEquals(i.hasBeenInspected(a), false);
			assertEquals(i.hasBeenInspected(b), false);
			assertEquals(i.hasBeenInspected(d), false);
			
			i.inspect(a, false);
			i.inspect(b, false);
			i.inspect(d, false);
			
			// Now we should have all of these inspected at this point 
			assertEquals(i.hasBeenInspected(a), true);
			assertEquals(i.hasBeenInspected(b), true);
			assertEquals(i.hasBeenInspected(d), true);
			
			
		}
	
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testInspectMethod()
	{
		try
		{	
			
			Inspector i = new Inspector();
			
			Object a = new ClassA(15);
			Object b = new ClassB();
			Object d = new ClassD();
	

			
			// Inspect objects
			i.inspect(a, false);
			i.inspect(b, false);
			i.inspect(d, false);
			
			//Inspected three objects so should have three object codes
			assertEquals(i.getObjectCodes().size(), 3);
			
			// Now first three objects has been inspected
		
			
	
			// Same class but different instance with recursion
			i.inspect(b, true);
			
			// We should have 6 object codes at this stage
			// Since we count b itself as 1, plus the two classes it has as fields
			
			assertEquals(i.getObjectCodes().size(), 6);
			
			
			// Now we count d itself plus one of its variables that are actually instantiated
			// So we expect 8 object codes here 
			i.inspect(d, true);
			assertEquals(i.getObjectCodes().size(), 8);
			
	
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testIsWrapperClass()
	{
		Inspector i = new Inspector();
		
		
		// Test with 3 wrapper classes 
		Integer in = new Integer(5);
		Short sh = new Short((short) 3);
		Boolean b = new Boolean(true);
		
		
		// All should be true 
		assertTrue(i.isWrapperClass(in));
		assertTrue(i.isWrapperClass(sh));
		assertTrue(i.isWrapperClass(b));
		
		
		// Shouldn't return true here 
		assertFalse(i.isWrapperClass(new int[5]));
		assertFalse(i.isWrapperClass("Nope"));
		
	}
	
	

}
