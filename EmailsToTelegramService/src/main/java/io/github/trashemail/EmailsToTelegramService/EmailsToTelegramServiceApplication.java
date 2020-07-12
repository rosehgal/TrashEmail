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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableAsync
public class EmailsToTelegramServiceApplication implements CommandLineRunner {

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
        Integer count = imapClientServiceConfig
                            .getImap()
                            .getEmails().size();

        List<CompletableFuture<Void>> jobs = new ArrayList<>();
        for(int i=0; i < count; ++i) {
            ImapClient finalImapClient = new ImapClient(
                    imapClientServiceConfig.getImap().getHost(),
                    imapClientServiceConfig.getImap().getPort(),
                    imapClientServiceConfig.getImap().getEmails().get(i),
                    imapClientServiceConfig.getImap().getPasswords().get(i)
            );

            CompletableFuture<Void> job_i = CompletableFuture.runAsync(
                    () -> {
                        try {
                            finalImapClient.fetchNewEmails();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
            jobs.add(job_i);
        }
        for(CompletableFuture job : jobs)
            CompletableFuture.allOf(job).join();
    }
}
