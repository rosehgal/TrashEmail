package io.github.trashemail.EmailsToTelegramService;

import io.github.trashemail.EmailsToTelegramService.Configuration.ImapClientServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class EmailsToTelegramServiceApplication implements CommandLineRunner {

    @Autowired
    ImapClient imapClient;

    @Autowired
    ImapClientServiceConfig imapClientServiceConfig;

    public static void main(String[] args) {
        SpringApplication
                .run(EmailsToTelegramServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Override
    public void run(String...args) throws Exception{
        List<String> emails = imapClientServiceConfig.getImap().getEmails();
        List<String> passwords =
                imapClientServiceConfig.getImap().getPasswords();

        for(int i=0; i<emails.size(); ++i)
            imapClient.fetchNewEmails(
                    emails.get(i),
                    passwords.get(i)
            );
    }
}
