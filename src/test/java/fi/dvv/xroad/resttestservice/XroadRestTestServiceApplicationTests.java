package fi.dvv.xroad.resttestservice;

import fi.dvv.xroad.resttestservice.model.ErrorDto;
import fi.dvv.xroad.resttestservice.model.GreetingDto;
import fi.dvv.xroad.resttestservice.model.RandomNumberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + contextPath;
    };
    
    @Test
    void randomReturnsRandomNumber() throws Exception {
        assertThat(this.restTemplate.getForObject(baseUrl() + "/random", RandomNumberDto.class).randomNumber()).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(100);
    }

    @Test
    void helloReturnsGreetingMessage() throws Exception {
        assertThat(this.restTemplate.getForObject(baseUrl() + "/hello", GreetingDto.class).greeting()).isEqualTo("Hello! Greetings from adapter server!");
    }

    @Test
    void helloReturnsGreetingMessageWithName() throws Exception {
        assertThat(this.restTemplate.getForObject(new URI(baseUrl() + "/hello?name=Gandalf"), GreetingDto.class).greeting()).isEqualTo("Hello Gandalf! Greetings from adapter server!");
    }

    @Test
    void helloReturnsGreetingMessageWithComplexName() throws Exception {
        assertThat(this.restTemplate.getForObject(new URI(baseUrl() + "/hello?name=X%20%C3%86%20A-12"), GreetingDto.class).greeting()).isEqualTo("Hello X Æ A-12! Greetings from adapter server!");
    }

    @Test
    void helloReturnsGreetingMessageWithNameEscapedForJson() throws Exception {
        assertThat(this.restTemplate.getForObject(baseUrl() + "/hello?name=<script>alert('Executed!');</script>", GreetingDto.class).greeting()).isEqualTo("Hello <script>alert('Executed!');<\\/script>! Greetings from adapter server!");
    }

    @Test
    void helloReturnsErrorForTooLongName() throws Exception {
        String name = "a".repeat(257);
        ErrorDto error = this.restTemplate.getForObject(baseUrl() + "/hello?name=" + name, ErrorDto.class);
        assertThat(error.errorMessage()).isEqualTo("Name is too long. Max length is 256 characters.");
        assertThat(error.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void nonExistentEndpointReturnsError() throws Exception {
        ErrorDto error = this.restTemplate.getForObject(baseUrl() + "/not-here", ErrorDto.class);
        assertThat(error.errorMessage()).isEqualTo("No endpoint GET /rest-api/not-here.");
        assertThat(error.httpStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void wrongMethodReturnsError() throws Exception {
        ErrorDto error = this.restTemplate.postForObject(baseUrl() + "/random", "foo", ErrorDto.class);
        assertThat(error.errorMessage()).isEqualTo("Request method 'POST' is not supported");
        assertThat(error.httpStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
    }
}
