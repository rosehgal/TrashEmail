package io.github.teledot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class TeledotApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TeledotApplication.class, args);
        System.out.println("Interaction " + ctx.containsBeanDefinition("EmailServerInteraction"));
        System.out.println("Config " + ctx.containsBeanDefinition("EmailServerConfig"));

    }

}
