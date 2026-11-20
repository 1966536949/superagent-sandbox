package com.controller;

import com.interceptor.AuthorizationInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HealthController()).build();

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void healthEndpointSkipsTokenVerification() throws Exception {
        AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/health");
        request.addHeader("Origin", "http://localhost:8080");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handler = new HandlerMethod(
                new HealthController(), HealthController.class.getMethod("health"));

        assertTrue(interceptor.preHandle(request, response, handler));
    }
}
