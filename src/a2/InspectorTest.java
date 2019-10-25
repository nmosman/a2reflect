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
	public void testInspect() {
		fail("Not yet implemented");
	}

}
