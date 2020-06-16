package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.DTO.TrashemailStats;
import io.github.trashemail.Respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TrashemailResource {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailServerConfig emailServerConfig;

    @GetMapping(value = "/stats")
    public TrashemailStats getStats(){
        TrashemailStats trashemailStats = new TrashemailStats();

        trashemailStats.setNumberOfUsers(
                userRepository.getDistinctChatIdCount()
        );
        trashemailStats.setNumberOfEmailsRegistered(
                userRepository.count()
        );


        Map<String, Long> domainCount = new HashMap<String, Long>();
        for(String domain: emailServerConfig.getHosts()){
            Long countForDomain = (
                    (Integer) userRepository.findByEmailIdEndsWith(
                        domain
                    ).size()
            ).longValue();

            domainCount.put(domain, countForDomain);
        }

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysBefore = calendar.getTime();

        trashemailStats.setEmailIdsCreatedToday(
                userRepository.getEmailIdsCreatedTodayCount()
        );

        trashemailStats.setEmailIdsCreatedInWeek(
                userRepository.getEmailIdsCreatedInWeek(
                        today,
                        sevenDaysBefore
                )
        );
        trashemailStats.setDomainsToNumbers(domainCount);
        
        return trashemailStats;
    }
}
