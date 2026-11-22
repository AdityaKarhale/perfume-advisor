package com.perfumeadvisor.ping.controller;

import com.perfumeadvisor.ping.service.PingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Slice test for PingController — loads only the web layer, not the full app.
 *
 * @WebMvcTest tells Spring Boot to auto-configure MockMvc and scan only
 * this controller (plus filters, converters, etc.), NOT services or repositories.
 * This keeps the test fast.
 *
 * @MockitoBean replaces the real PingService with a Mockito mock in the Spring
 * context. (Spring Boot 4 removed the old @MockBean; @MockitoBean is the
 * replacement from Spring Framework's bean-override support.)
 *
 * We stub the mock's return values with when(...).thenReturn(...), then use
 * MockMvc to simulate an HTTP GET and assert the JSON response.
 */
@WebMvcTest(PingController.class)
class PingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PingService pingService;

    @Test
    void pingReturnsNameAndVersion() throws Exception {
        when(pingService.getAppName()).thenReturn("Perfume Advisor Backend");
        when(pingService.getAppVersion()).thenReturn("0.0.1-SNAPSHOT");

        mockMvc.perform(get("/api/v1/ping"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Perfume Advisor Backend"))
                .andExpect(jsonPath("$.version").value("0.0.1-SNAPSHOT"));
    }
}
