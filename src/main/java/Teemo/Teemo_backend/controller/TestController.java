package Teemo.Teemo_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @GetMapping("/")
    public ResponseEntity hello(){
        log.info("test 통과");
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
