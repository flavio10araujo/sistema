package com.polifono.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.polifono.AbstractControllerTest;

/**
 * Unit tests for the CustomErrorController using Spring MVC Mocks.
 * <p>
 * These tests utilize the Spring MVC Mock objects to simulate sending actual HTTP requests to the Controller component.
 * This test ensures that the RequestMappings are configured correctly.
 * Also, these tests ensure that the request and response bodies are serialized as expected.
 */
public class CustomErrorControllerTest /*extends AbstractControllerTest*/ {

    /*@BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void whenError404Occurs_OpenError404Page() throws Exception {
        String uri = "/error404";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertFalse(content.trim().isEmpty(), "failure - expected HTTP response body to have a value");
    }

    @Test
    public void whenError500Occurs_OpenError500Page() throws Exception {
        String uri = "/error500";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertFalse(content.trim().isEmpty(), "failure - expected HTTP response body to have a value");
    }*/
}
