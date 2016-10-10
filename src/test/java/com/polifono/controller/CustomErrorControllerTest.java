package com.polifono.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractControllerTest;

/**
 * Unit tests for the CustomErrorController using Spring MVC Mocks.
 * 
 * These tests utilize the Spring MVC Mock objects to simulate sending actual HTTP requests to the Controller component. 
 * This test ensures that the RequestMappings are configured correctly. 
 * Also, these tests ensure that the request and response bodies are serialized as expected.
 * 
 */
@Transactional
public class CustomErrorControllerTest extends AbstractControllerTest {

	private final int HTTP_STATUS_OK = 200;
	private final int HTTP_STATUS_REDIRECT = 302;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void error() throws Exception {
		String uri = "/error";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_REDIRECT, status);
	}
	
	@Test
	public void error404() throws Exception {
		String uri = "/error404";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		String content = result.getResponse().getContentAsString(); // HTML page sent to the browser.
        int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_OK, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}
	
	@Test
	public void error500() throws Exception {
		String uri = "/error500";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_OK, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}
}