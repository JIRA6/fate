package jira6.fate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class FateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FateApplication.class, args);
    }

}
