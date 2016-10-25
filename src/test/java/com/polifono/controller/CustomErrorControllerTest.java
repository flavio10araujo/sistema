package com.polifono.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polifono.AbstractControllerTest;

/**
 * Unit tests for the CustomErrorController using Spring MVC Mocks.
 * 
 * These tests utilize the Spring MVC Mock objects to simulate sending actual HTTP requests to the Controller component. 
 * This test ensures that the RequestMappings are configured correctly. 
 * Also, these tests ensure that the request and response bodies are serialized as expected.
 * 
 */
public class CustomErrorControllerTest extends AbstractControllerTest {
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void error_WhenGenericErrorOccurs_RedirectToHomePage() throws Exception {
		String uri = "/error";
		
		/*MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_REDIRECT, status);*/
		
		mvc.perform(MockMvcRequestBuilders.get(uri))
        	.andExpect(status().is3xxRedirection()) // Verify if the HTTP return is a redirect code (302).
        	.andExpect(view().name("redirect:/")); // Verify if the system has redirected to the right place.
	}
	
	@Test
	public void error404_WhenErro404Occurs_OpenError404Page() throws Exception {
		String uri = "/error404";
		
		/*MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		String content = result.getResponse().getContentAsString(); // HTML page sent to the browser.
        int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_OK, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);*/
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(status().isOk()) // Verify if the HTTP return is 200.
				.andExpect(view().name("error/404")) // Verify is the system has loaded the right view mapping. 
				//.andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp")) // Verify is the system has loaded the right view file.
				.andReturn();
		
		String content = result.getResponse().getContentAsString();
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}
	
	@Test
	public void error500_WhenError500Occurs_OpenError500Page() throws Exception {
		String uri = "/error500";
		
		/*MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();
        Assert.assertEquals("failure - expected HTTP status", HTTP_STATUS_OK, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);*/
        
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri))
				.andExpect(status().isOk()) // Verify if the HTTP return is 200.
				.andExpect(view().name("error/500")) // Verify is the system has loaded the right view mapping. 
				//.andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp")) // Verify is the system has loaded the right view file.
				.andReturn();
		
		String content = result.getResponse().getContentAsString();
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}
}