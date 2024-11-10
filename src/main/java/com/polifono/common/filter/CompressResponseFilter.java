package com.polifono.common.filter;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This filter is used to compress the response content.
 */
@Component
public class CompressResponseFilter implements Filter {

    private HtmlCompressor compressor;

    @Override
    public void init(FilterConfig config) throws ServletException {
        compressor = new HtmlCompressor();
        compressor.setCompressCss(true);
        //compressor.setCompressJavaScript(true); // Warning: Do not use! It does not work well with Thymeleaf.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (!shouldCompress(req.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);
            filterChain.doFilter(request, capturingResponseWrapper);

            if (response.getContentType() != null) {
                String content = capturingResponseWrapper.getCaptureAsString();
                response.getWriter().write(compressor.compress(content));
            }
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Return true if the content need to be compressed.
     */
    private boolean shouldCompress(String uri) {
        Set<String> excludedExtensions = Set.of(".js", ".css", ".ico", ".png", ".jpg", ".gif", ".bmp", ".pdf");
        Set<String> excludedPaths = Set.of("/static/", "/vendors/", "/diplomas/");

        for (String ext : excludedExtensions) {
            if (uri.contains(ext)) {
                return false;
            }
        }

        for (String path : excludedPaths) {
            if (uri.contains(path)) {
                return false;
            }
        }

        return true;
    }
}
