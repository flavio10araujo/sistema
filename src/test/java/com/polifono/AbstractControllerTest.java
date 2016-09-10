package com.polifono;

import org.springframework.test.context.web.WebAppConfiguration;

/**
 * This class extends the functionality of AbstractTest. 
 * AbstractControllerTest is the parent of all web controller unit test classes.
 * The class ensures that a type of WebApplicationContext is built 
 * and prepares a MockMvc instance for use in test methods.
 * 
 */
@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractTest {

}