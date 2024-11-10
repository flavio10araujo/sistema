package com.polifono.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class CompressResponseFilterIT {

    @InjectMocks
    private CompressResponseFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() throws ServletException {
        filter.init(null);
    }

    @Test
    @Disabled
    public void givenRequestToContentThatShouldBeCompressed_WhenDoFilter_ThenCompressResponse() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("test.html");
        when(response.getContentType()).thenReturn("text/html");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper(response);
        /*doAnswer(invocation -> {
            capturingResponseWrapper.getOutputStream().write("Original Content".getBytes());
            return null;
        }).when(filterChain).doFilter(any(ServletRequest.class), any(HtmlResponseWrapper.class));*/

        // When
        filter.doFilter(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, capturingResponseWrapper);
        verify(response).getWriter();
        String compressedContent = stringWriter.toString();
        assertFalse(compressedContent.isEmpty());
        assertTrue(compressedContent.contains("Original Content")); // Adjust this based on actual compression logic
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test.ico",
            "test.png",
            "test.jpg",
            "test.gif",
            "test.bmp",
            "test.pdf",
            "test.js",
            "test.css",
            "/static/test.html",
            "/vendors/test.html",
            "/diplomas/code" })
    public void givenRequestToContentThatShouldNotBeCompressed_WhenDoFilter_ThenDoNotCompressResponse(String uri) throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(uri);
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }
}
