package com.polifono;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The AbstractTest class is the parent of all JUnit test classes.
 * This class configures the test ApplicationContext and test runner environment.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public abstract class AbstractTest {

    /**
     * The Logger instance for all classes in the unit test framework.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
