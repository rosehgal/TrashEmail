package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.DTO.TrashemailStats;
import io.github.trashemail.Respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

        trashemailStats.setDomainsToNumbers(domainCount);
        trashemailStats.setApplicationStatus(true);
        trashemailStats.setDbConnection(true);


        return trashemailStats;
    }
}
