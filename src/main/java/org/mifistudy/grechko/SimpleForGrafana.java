package org.mifistudy.grechko;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class SimpleForGrafana {
    private static final Logger log = LoggerFactory.getLogger(SimpleForGrafana.class);

    public static void main(String[] args) {
        log.info("Starting SimpleForGrafana!");
        SpringApplication.run(SimpleForGrafana.class, args);
        log.info("Started SimpleForGrafana ok!");
    }
}

@RestController
class TestController {

    private final List<byte[]> memoryHog = new ArrayList<>();
    private final int blockSize = 1024*1024;
    private final int peaceSize = 100; // 100 Mb

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final Counter requestCounter;

    public TestController(MeterRegistry registry) {
        this.requestCounter = Counter.builder("app.requests.total")
                .description("Total number of requests")
                .tag("endpoint", "/hello")
                .register(registry);
        log.info("Test controller runs!");
    }

    @GetMapping("/hello")
    public String hello() {
        requestCounter.increment();
        log.info("Hello Endpoint request: {}", requestCounter.count());
        return "Hello from Spring Boot!";
    }

    @GetMapping("/metrics-test")
    public String metricsTest() {
        memoryHog.add(new byte[blockSize*peaceSize]);

        log.info("Metrics-test Endpoint request! App {} Mb used", memoryHog.size()*peaceSize);
        return "Metrics test endpoint";
    }
}
