package com.example.Order_Service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@ContextConfiguration(classes = {SecurityConfig.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class SecurityConfigDiffblueTest {
    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private SecurityConfig securityConfig;

    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("Test corsConfigurationSource(); given SecurityConfig (default constructor)")
    @Tag("MaintainedByDiffblue")
    void testCorsConfigurationSource_givenSecurityConfig() {
        // Arrange and Act
        CorsConfigurationSource actualCorsConfigurationSourceResult = (new SecurityConfig()).corsConfigurationSource();
        CorsConfiguration actualCorsConfiguration = actualCorsConfigurationSourceResult
                .getCorsConfiguration(new MockHttpServletRequest());

        // Assert
        assertTrue(actualCorsConfigurationSourceResult instanceof UrlBasedCorsConfigurationSource);
        Map<String, CorsConfiguration> corsConfigurations = ((UrlBasedCorsConfigurationSource) actualCorsConfigurationSourceResult)
                .getCorsConfigurations();
        assertEquals(1, corsConfigurations.size());
        CorsConfiguration getResult = corsConfigurations.get("/**");
        assertNull(getResult.getAllowPrivateNetwork());
        assertNull(getResult.getMaxAge());
        assertNull(getResult.getAllowedOriginPatterns());
        assertNull(getResult.getExposedHeaders());
        assertEquals(1, getResult.getAllowedHeaders().size());
        assertEquals(1, getResult.getAllowedOrigins().size());
        assertEquals(5, getResult.getAllowedMethods().size());
        assertTrue(getResult.getAllowCredentials());
        assertSame(getResult, actualCorsConfiguration);
    }

    @Test
    @DisplayName("Test corsConfigurationSource(); then MockHttpServletRequest() Parts List")
    @Tag("MaintainedByDiffblue")
    void testCorsConfigurationSource_thenMockHttpServletRequestPartsList() throws ServletException, IOException {
        // Arrange and Act
        CorsConfigurationSource actualCorsConfigurationSourceResult = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration actualCorsConfiguration = actualCorsConfigurationSourceResult.getCorsConfiguration(request);

        // Assert
        assertTrue(securityConfig instanceof com.example.Order_Service.security.SecurityConfig);
        Collection<Part> parts = request.getParts();
        assertTrue(parts instanceof List);
        assertTrue(request.getHttpServletMapping() instanceof MockHttpServletMapping);
        assertTrue(request.getSession() instanceof MockHttpSession);
        assertTrue(request.getServletContext() instanceof MockServletContext);
        assertTrue(actualCorsConfigurationSourceResult instanceof UrlBasedCorsConfigurationSource);
        assertEquals("", request.getContextPath());
        assertEquals("", request.getMethod());
        assertEquals("", request.getProtocolRequestId());
        assertEquals("", request.getRequestId());
        assertEquals("", request.getRequestURI());
        assertEquals("", request.getServletPath());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("http", request.getScheme());
        assertEquals("localhost", request.getLocalName());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("localhost", request.getServerName());
        assertNull(request.getContentAsByteArray());
        assertNull(request.getAsyncContext());
        assertNull(request.getCookies());
        assertNull(request.getAuthType());
        assertNull(request.getCharacterEncoding());
        assertNull(request.getContentType());
        assertNull(request.getPathInfo());
        assertNull(request.getPathTranslated());
        assertNull(request.getQueryString());
        assertNull(request.getRemoteUser());
        assertNull(request.getRequestedSessionId());
        assertNull(request.getUriTemplate());
        assertNull(request.getUserPrincipal());
        assertEquals(-1, request.getContentLength());
        assertEquals(-1L, request.getContentLengthLong());
        Map<String, CorsConfiguration> corsConfigurations = ((UrlBasedCorsConfigurationSource) actualCorsConfigurationSourceResult)
                .getCorsConfigurations();
        assertEquals(1, corsConfigurations.size());
        assertEquals(80, request.getLocalPort());
        assertEquals(80, request.getRemotePort());
        assertEquals(80, request.getServerPort());
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertFalse(request.isAsyncStarted());
        assertFalse(request.isAsyncSupported());
        assertFalse(request.isRequestedSessionIdFromURL());
        assertTrue(request.isTrailerFieldsReady());
        assertTrue(parts.isEmpty());
        assertTrue(corsConfigurations.containsKey("/**"));
        assertTrue(request.getTrailerFields().isEmpty());
        assertTrue(request.getParameterMap().isEmpty());
        assertTrue(request.isActive());
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertTrue(request.isRequestedSessionIdValid());
        assertSame(corsConfigurations.get("/**"), actualCorsConfiguration);
    }
}
