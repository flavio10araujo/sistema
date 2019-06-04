package com.polifono.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

@Component
public class CompressResponseFilter implements Filter {

	private HtmlCompressor compressor;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		if (!isCompress(req.getRequestURI())) {
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
	public void init(FilterConfig config) throws ServletException {
		compressor = new HtmlCompressor();
		compressor.setCompressCss(true);
		compressor.setCompressJavaScript(true);
	}

	@Override
	public void destroy() {
	}

	/**
	 * Return true if the content need to be compress.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean isCompress(String uri) {
		if (uri.contains(".js") ||
				uri.contains(".css") || 
				uri.contains(".ico") || 
				uri.contains(".png") || 
				uri.contains(".jpg") ||
				uri.contains(".gif") ||
				uri.contains(".bmp") ||
				uri.contains(".pdf")) {
			
			return false;
		}
		
		if (uri.contains("/static/") || uri.contains("/vendors/") || uri.contains("/diploma/")) {
			return false;
		}

		return true;
	}
}