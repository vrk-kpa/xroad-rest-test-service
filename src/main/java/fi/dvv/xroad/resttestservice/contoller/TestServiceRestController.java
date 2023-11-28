package fi.dvv.xroad.resttestservice.contoller;

import fi.dvv.xroad.resttestservice.model.ErrorDto;
import fi.dvv.xroad.resttestservice.model.GreetingDto;
import fi.dvv.xroad.resttestservice.model.RandomNumberDto;
import org.owasp.esapi.ESAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class TestServiceRestController {
    final private Random randomGenerator = new Random();
    final private int maxRandom = 101;

    @GetMapping("/random")
    public RandomNumberDto getRandomInt() {
        System.out.println("called /random");
        return new RandomNumberDto(randomGenerator.nextInt(maxRandom));
    }

    @GetMapping("/greeting")
    public GreetingDto getGreeting(@RequestParam(value = "name", defaultValue = "") String name) {
        System.out.println("called /greeting");

        String nameOut = "";

        if(!stringIsEmpty(name)) {
            validateName(name);
            nameOut = " " + ESAPI.encoder().encodeForJSON(name);
        }

        return new GreetingDto("Hello" + nameOut + "! Greetings from adapter server!");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(
                new ErrorDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    private Boolean stringIsEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
    private void validateName(String name) {
        if (name.length() > 256)
            throw new IllegalArgumentException("Name is too long. Max length is 256 characters.");
    }
}
