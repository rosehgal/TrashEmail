package io.github.teledot.EmailInteraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@EnableScheduling
public class FetchEmailAndSendTelegramMessages {

    private static final Logger log = LoggerFactory.getLogger(FetchEmailAndSendTelegramMessages.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 4000)
    public void reportCurrentTime() {

    }
}