package com.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    private final ScheduledExecutorService executorPool;
    private final AtomicInteger counter;

    public DemoController() {
        executorPool = Executors.newScheduledThreadPool(1);
        counter = new AtomicInteger(0);
    }

    @PostConstruct
    public void start(){
        executorPool.scheduleAtFixedRate(() -> {
            log.info("log something count = {}", counter.incrementAndGet());

        }, 5, 10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stop() {
        executorPool.shutdown();
        executorPool.shutdownNow();
    }

    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String hello() {
        String msg = "Hello world";
        log.info(msg);
        return msg;
    }

    @GetMapping(value = "/errorLog", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String logError() {
        log.error("this is an error", new RuntimeException("oops, there is a bug in the app !"));
        return "error has been logged";
    }
}
