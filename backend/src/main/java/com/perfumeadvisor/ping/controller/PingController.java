package com.perfumeadvisor.ping.controller;

import com.perfumeadvisor.ping.service.PingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for the health-check / ping endpoint.
 *
 * @RestController = @Controller + @ResponseBody, so every method's return value
 * is serialised straight to JSON (via Jackson) instead of resolving a view template.
 *
 * @RequestMapping("/api/v1") sets the base path for all endpoints in this controller,
 * giving us a versioned API namespace from day one.
 *
 * Constructor injection: Spring automatically passes the PingService bean.
 * We store it in a final field — the controller is immutable after construction.
 */
@RestController
@RequestMapping("/api/v1")
public class PingController {

    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    /**
     * GET /api/v1/ping
     *
     * Returns a JSON object with the application name and version.
     * Example response: {"name":"Perfume Advisor Backend","version":"0.0.1-SNAPSHOT"}
     *
     * Map.of() creates an unmodifiable map. Jackson serialises it to JSON automatically.
     */
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of(
                "name", pingService.getAppName(),
                "version", pingService.getAppVersion()
        );
    }
}
