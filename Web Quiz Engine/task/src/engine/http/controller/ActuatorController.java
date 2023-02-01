package engine.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActuatorController {
    @PostMapping(value = "/actuator/shutdown")
    @ResponseBody
    public ResponseEntity<Object> shutdown(
    ) {
        return new ResponseEntity<>("Valid", HttpStatus.OK);

    }
}
