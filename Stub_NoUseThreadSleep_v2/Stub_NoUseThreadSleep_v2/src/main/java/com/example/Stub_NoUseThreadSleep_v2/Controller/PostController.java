package com.example.Stub_NoUseThreadSleep_v2.Controller;

import com.example.Stub_NoUseThreadSleep_v2.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@RestController
public class PostController {

    final Config config = new Config();
    private static final Logger log = LogManager.getLogger(PostController.class);

    final private Map<String, String> goodMessage = Map.of("good_message", "pong");
    final private Map<String, String> badMessage = Map.of("bad_message", "it's not OK");
    final private String check_message = "ping";


    @PostMapping(value = "/stub/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Map<String, String>>> miniPingPong(@RequestBody Dto dto) {

        log.info("Получили - {}", dto.getMessage());

        ///  Основная часть кода, где происходит задержка
        ///
        /// Для ассинхронного выполнения задач используем класс CompletableFuture
        ///  и метод delayedExecutor котороый не блокирует поток
        return CompletableFuture.supplyAsync(() -> {

            log.info("Отправляем ответ\n");

            if (dto.getMessage().equals(check_message)) {

                return ResponseEntity.status(HttpStatus.OK).body(goodMessage);

            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badMessage);
            }

        }, CompletableFuture.delayedExecutor(config.getDelay(), TimeUnit.MILLISECONDS));
    }
}
