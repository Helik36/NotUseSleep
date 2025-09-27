package com.example.Stub_NoUseThreadSleep_v2.Controller;

import com.example.Stub_NoUseThreadSleep_v2.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@RestController
public class PostController {

    Config config = new Config();
    Dto dto = new Dto();
    private static final Logger log = LogManager.getLogger(PostController.class);

    private final Object lock = new Object(); // Для wait

    final private Map<String, String> select_response = Map.of(
            "good_message", "pong",
            "bad_message", "it's not OK"
    );

    final private String check_meesage = "ping";


    @PostMapping(value = "/stub/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Map<String, String>>> getMessage(@PathVariable String get_message) {


        log.info("get - {}", get_message);

        /// Сначала думал использовать wait, но обдмав

//        try {
//            synchronized (lock) {
//
//                // имитируем задержку
//                lock.wait(config.getDelay());
//
//                log.info("Отправляем ответ");
//                return ResponseEntity.status(HttpStatus.OK).body(response);
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }


            CompletableFuture<Object> completableFuture = new CompletableFuture<>();
            return CompletableFuture.supplyAsync(() -> {

                log.info("Отправляем ответ\n");
                if (dto.getMessage().equals(check_meesage)) {

                    String key = "good_message";

                    return ResponseEntity.status(HttpStatus.OK).body(Map.of(key, select_response.get(key)));
                } else {

                    String key = "good_message";


                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(key, select_response.get(key)));
                }

                }, CompletableFuture.delayedExecutor(config.getDelay(), TimeUnit.MILLISECONDS));
    }
}
