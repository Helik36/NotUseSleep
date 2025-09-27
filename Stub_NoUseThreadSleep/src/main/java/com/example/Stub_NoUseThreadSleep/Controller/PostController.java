package com.example.Stub_NoUseThreadSleep.Controller;

import com.example.Stub_NoUseThreadSleep.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class PostController {

    Config config = new Config();
    private static final Logger log = LogManager.getLogger(PostController.class);

    @GetMapping(value = "/stub/ping")
    public ResponseEntity<Map<String, String>> getMessage(@PathVariable String get_message) {

        log.info("get - %s", get_message);
        Map<String, String> response = new HashMap<>();

        response.put("message", get_message);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
