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
            HtmlResponseWrapper capturingResponseWrapper = createHtmlResponseWrapper(response);
            filterChain.doFilter(request, capturingResponseWrapper);
            compressAndWriteResponse(response, capturingResponseWrapper);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * Return true if the content need to be compressed.
     */
    private boolean shouldCompress(String uri) {
        return !isExcludedExtension(uri) && !isExcludedPath(uri);
    }

    private boolean isExcludedExtension(String uri) {
        Set<String> excludedExtensions = Set.of(".js", ".css", ".ico", ".png", ".jpg", ".gif", ".bmp", ".pdf");
        for (String ext : excludedExtensions) {
            if (uri.contains(ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcludedPath(String uri) {
        Set<String> excludedPaths = Set.of("/static/", "/vendors/", "/diplomas/");
        for (String path : excludedPaths) {
            if (uri.contains(path)) {
                return true;
            }
        }
        return false;
    }

    private HtmlResponseWrapper createHtmlResponseWrapper(ServletResponse response) {
        return new HtmlResponseWrapper((HttpServletResponse) response);
    }

    private void compressAndWriteResponse(ServletResponse response, HtmlResponseWrapper capturingResponseWrapper) throws IOException {
        if (response.getContentType() != null) {
            String content = capturingResponseWrapper.getCaptureAsString();
            response.getWriter().write(compressor.compress(content));
        }
    }
}
