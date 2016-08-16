package com.polifono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculateTest {
	
	Calculate calculation = new Calculate();
	int sum = calculation.sum(2, 5);
	int testSum = 10;

	@Test
	public void testSum() {
		System.out.println("@Test sum(): " + sum + " = " + testSum);
		assertEquals(sum, testSum);
	}
}