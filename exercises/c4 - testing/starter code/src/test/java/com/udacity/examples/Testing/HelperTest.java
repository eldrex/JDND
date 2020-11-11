package com.udacity.examples.Testing;




import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class HelperTest {

	@Test
	public void test(){
		assertTrue(true);
	}


	@Test
	public void mergeListTest(){
		List<String> emps = Arrays.asList("john", "paul");
		final String mergedList = Helper.getMergedList(emps);
		assertEquals("john, paul", mergedList);
	}
}
