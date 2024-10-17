package com.polifono.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    @InjectMocks
    private CustomErrorController customErrorController;

    @Test
    public void whenError404_thenReturnError404Page() {
        String result = customErrorController.error404();
        Assertions.assertEquals("error/404", result);
    }

    @Test
    public void whenError500_thenReturnError500Page() {
        String result = customErrorController.error500();
        Assertions.assertEquals("error/500", result);
    }
}
