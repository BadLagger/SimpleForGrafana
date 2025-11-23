package org.mifistudy.grechko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;


@SpringBootApplication
public class SimpleForGrafana {
    public static void main(String[] args) {
        System.out.println("Hello SimpleForGrafana!");
        SpringApplication.run(SimpleForGrafana.class, args);
    }
}

@RestController
class TestController {
    private final Counter requestCounter;

    public TestController(MeterRegistry registry) {
        this.requestCounter = Counter.builder("app.requests.total")
                .description("Total number of requests")
                .tag("endpoint", "/hello")
                .register(registry);
    }

    @GetMapping("/hello")
    public String hello() {
        requestCounter.increment();
        return "Hello from Spring Boot!";
    }

    @GetMapping("/metrics-test")
    public String metricsTest() {
        // Этот эндпоинт будет генерировать метрики
        return "Metrics test endpoint";
    }
}
