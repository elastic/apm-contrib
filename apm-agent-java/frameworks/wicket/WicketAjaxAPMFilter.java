/*
 * Copyright 2019 Elastic and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.elastic.apm.contrib.wicket;

import co.elastic.apm.api.ElasticApm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A servlet filter that intercepts Wicket AJAX requests to set valid names of Elastic APM transactions.
 * The transaction name consists of a resource path without resource ID, Wicket Ajax listener ID
 * and the associated Wicket component ID. E.g.: example/path/to/resource:IBehaviorListener.0-example_component_id
 *
 * @author Martin Dindoffer
 */
public class WicketAjaxAPMFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(WicketAjaxAPMFilter.class);
    private static final Pattern RESOURCE_ID_PATTERN = Pattern.compile("/\\d+");
    private static final char APM_PATH_DELIMITER = ':';
    private static final char WICKET_COMPONENT_DELIMITER = '-';
    private static final char QUERY_PARAM_DELIMITER = '&';
    private static final char WICKET_INTERFACE_PREFIX = 'I';
    private static final char TILDE_CHAR = '~';
    private static final char UNDERSCORE_CHAR = '_';

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String query = httpRequest.getQueryString();
        if ((query != null) && containsWicketListener(query)) {
            String resourcePath = removeFirstChar(stripResourceIDs(extractResourcePath(httpRequest)));
            String wicketIdentifier = encodeTildesForKibanaWorkaround(stripParentComponents(extractWicketComponentPath(query)));

            String transactionName = resourcePath + APM_PATH_DELIMITER + wicketIdentifier;
            LOG.trace("AJAX transaction name: {}", transactionName);
            ElasticApm.currentTransaction().setName(transactionName);
        }
        chain.doFilter(request, response);
    }

    /**
     * Checks whether the query string mentions one of Wicket AJAX listeners.
     *
     * @param queryString a query string
     * @return true if the string contains an AJAX listener, false otherwise
     */
    private static boolean containsWicketListener(String queryString) {
        return queryString.contains("IResourceListener")
                || queryString.contains("IBehaviorListener")
                || queryString.contains("ILinkListener")
                || queryString.contains("IOnChangeListener")
                || queryString.contains("IFormSubmitListener");
    }

    /**
     * Extracts a "Resource" path from an http request. Resource path consists of servletPath and pathInfo,
     * i.e. omits the contextPath, since that would not provide much relevance.
     *
     * @param httpRequest HTTP request to process
     * @return extracted resource path
     */
    private static String extractResourcePath(HttpServletRequest httpRequest) {
        String pathInfo = httpRequest.getPathInfo();
        return (pathInfo == null) ? httpRequest.getServletPath() : (httpRequest.getServletPath() + pathInfo);
    }

    /**
     * Removes numeric resource identifiers from a resource path / URL.
     *
     * @param resourcePath a resource path to process
     * @return a resource path without resource identifiers
     */
    private static String stripResourceIDs(String resourcePath) {
        return RESOURCE_ID_PATTERN.matcher(resourcePath).replaceAll("");
    }

    /**
     * Removes a first char from a string.
     *
     * @param request string to remove from
     * @return string without a first char
     */
    private static String removeFirstChar(String request) {
        return request.substring(1);
    }

    /**
     * Extracts a wicket component path parameter pointing to target AJAX component from the HTTP query.
     *
     * @param query query string to process
     * @return component path parameter
     */
    private static String extractWicketComponentPath(String query) {
        int firstAmpIndex = query.indexOf(QUERY_PARAM_DELIMITER);
        int firstIIndex = query.indexOf(WICKET_INTERFACE_PREFIX);
        return (firstAmpIndex > 0) ? query.substring(firstIIndex, firstAmpIndex) : query.substring(firstIIndex);
    }

    /**
     * Removes Wicket parent hierarchy from a component path and leaves only the listener and target component ID.
     *
     * @param componentPath Wicket component path to process
     * @return Wicket AJAX listener identifier without parent hierarchy
     */
    private static String stripParentComponents(String componentPath) {
        int firstDashIndex = componentPath.indexOf(WICKET_COMPONENT_DELIMITER);
        int lastDashIndex = componentPath.lastIndexOf(WICKET_COMPONENT_DELIMITER);
        return componentPath.substring(0, firstDashIndex) + componentPath.substring(lastDashIndex);
    }

    /**
     * Replace all tildes in wicket component identifier with underscores. This is a workaround for Kibana's special
     * handling of the tilde character, that gets replaced by the '%' sign in javascript, resulting in an invalid URI.
     *
     * @param wicketIdentifier wicket component identifier
     * @return encoded component identifier
     * @see <a href="https://github.com/elastic/kibana/issues/24892">https://github.com/elastic/kibana/issues/24892</a>
     */
    private static String encodeTildesForKibanaWorkaround(String wicketIdentifier) {
        return wicketIdentifier.replace(TILDE_CHAR, UNDERSCORE_CHAR);
    }

    @Override
    public void destroy() {
    }
}
