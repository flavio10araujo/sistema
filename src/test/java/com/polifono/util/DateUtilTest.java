package com.polifono.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void formatDate_null() {
		assertEquals(DateUtil.formatDate(null), "");
	}
	
	@Test
	public void formatDate_empty() {
		assertEquals(DateUtil.formatDate(""), "");
	}
	
	@Test
	public void formatDate_wrongDate() {
		//assertEquals(DateUtil.formatDate("ERROR"), "");
		//assertEquals(DateUtil.formatDate("12345"), "");
		assertEquals(DateUtil.formatDate(new Date()), "");
	}
	
	
}