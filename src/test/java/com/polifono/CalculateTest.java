package com.polifono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculateTest {
	
	/*Calculate calculation = new Calculate();
	int sum = calculation.sum(2, 5);
	int testSum = 10;*/

	@Test
	public void sum_two_positives() {
		Calculate calculation = new Calculate();
		int sum = calculation.sum(1, 2);
		int testSum = 3;
		System.out.println("@Test sum(): " + sum + " = " + testSum);
		assertEquals(sum, testSum);
	}

	@Test
	public void sum_positive_and_zero() {
		Calculate calculation = new Calculate();
		int sum = calculation.sum(3, 0);
		int testSum = 3;
		System.out.println("@Test sum(): " + sum + " = " + testSum);
		assertEquals(sum, testSum);
	}
	
	@Test
	public void sum_two_negatives() {
		Calculate calculation = new Calculate();
		int sum = calculation.sum(-2, -3);
		int testSum = -5;
		System.out.println("@Test sum(): " + sum + " = " + testSum);
		assertEquals(sum, testSum);
	}
	
	@Test
	public void sum_negative_and_zero() {
		Calculate calculation = new Calculate();
		int sum = calculation.sum(-3, 0);
		int testSum = -3;
		System.out.println("@Test sum(): " + sum + " = " + testSum);
		assertEquals(sum, testSum);
	}
}