package servlet.mvc11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Mvc11Application {

    public static void main(String[] args) {
        SpringApplication.run(Mvc11Application.class, args);
    }

}
