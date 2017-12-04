package com.ing.direct.common.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * {@link javax.servlet.Filter} that handles potential XSS attacks added to request parameters.
 * This filter will primarily be used to sanitize (encode and strip out malicious block of code)
 * the parameters sent to the server.
 *
 * @see XssRequestWrapper
 */
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }

}
