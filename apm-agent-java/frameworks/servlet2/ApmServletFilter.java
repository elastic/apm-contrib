import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.HeaderExtractor;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Transaction;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter allows to capture transactions with Servlet 2.x that aren't instrumented automatically.
 * <br/>
 * It needs to be set first in the filter chain to properly wrap all requests.
 */
public class ApmServletFilter implements Filter {

    private static class ServletHeaderExtractor implements HeaderExtractor {

        private final HttpServletRequest request;

        public ServletHeaderExtractor(HttpServletRequest request) {
            this.request = request;
        }

        @Override
        public String getFirstHeader(String headerName) {
            return request.getHeader(headerName);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // using the 'remote parent' will make transaction inherit upstream transaction, if any.
        Transaction transaction = ElasticApm.startTransactionWithRemoteParent(new ServletHeaderExtractor(httpRequest));

        // use servlet path as transaction name
        String name = httpRequest.getServletPath();
        transaction.setName(name);

        Scope scope = transaction.activate();
        try {
            // execute the request as usual
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            scope.close();
        }

    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
