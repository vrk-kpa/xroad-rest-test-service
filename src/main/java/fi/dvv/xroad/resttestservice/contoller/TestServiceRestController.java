package fi.dvv.xroad.resttestservice.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestServiceRestController {
    @GetMapping("/random")
    public String getRandomInt() {
        int random_int = (int)Math.floor(Math.random() * 100);
        System.out.println("Got random: " + random_int);
        return String.valueOf(random_int);
    }

    @GetMapping("/greeting")
    public String getGreeting(@RequestParam(value = "name", defaultValue = "") String name) {
        System.out.println("Greeting with name: " + name);
        if(name != null && !name.trim().isEmpty()) {
            return "Hello " + name + "!";
        }
        return "Hello!";
    }
}

