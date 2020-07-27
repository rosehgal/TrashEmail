package io.github.trashemail;

import io.github.trashemail.Respositories.FreeUserIdRepository;
import io.github.trashemail.Respositories.UsedUserIdRepository;
import io.github.trashemail.Respositories.UserRepository;
import io.github.trashemail.Telegram.TelegramRequestHandler;
import io.github.trashemail.models.FreeUserId;
import io.github.trashemail.models.UsedUserId;
import io.github.trashemail.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class AutoGenerateEmailIdCleaner {
    @Autowired
    FreeUserIdRepository freeUserIdRepository;
    @Autowired
    UsedUserIdRepository usedUserIdRepository;
    @Autowired
    TelegramRequestHandler telegramRequestHandler;
    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(
            TelegramRequestHandler.class);

    /*
    This will run every 1 min.
    */
    @Scheduled(cron = "0 */1 * * * *")
    public void cleanGeneratedIds() throws Exception {
        log.info("Performing clean ...");

        List<UsedUserId> toDelete =
                usedUserIdRepository.getUserIdsCreatedBeforeTenMinutes();

        for (UsedUserId usedUserId : toDelete) {
            User user = userRepository.findByEmailIdAndIsActiveTrue(usedUserId.getUserId());
            telegramRequestHandler.deleteEmail(user);
        }
    }
}
