package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.Configurations.TrashemailConfig;
import io.github.trashemail.DTO.CreateEmailRequest;
import io.github.trashemail.DTO.CreateEmailResponse;
import io.github.trashemail.DTO.TrashemailStats;
import io.github.trashemail.models.EmailAllocation;
import io.github.trashemail.repositories.EmailCounterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
public class TrashemailResource {


    @Autowired
    EmailCounterRepository emailCounterRepository;

    @Autowired
    EmailServerConfig emailServerConfig;

    @Autowired
    TrashemailConfig trashemailConfig;

    @Autowired
    EmailServerInteraction emailServerInteraction;

    @PostMapping(value = "/create")
    public ResponseEntity<CreateEmailResponse> createEmailId(@RequestBody CreateEmailRequest createEmailRequest){
        EmailAllocation emailAllocation = new EmailAllocation();
        CreateEmailResponse emailResponse = new CreateEmailResponse();

        try{
            emailAllocation.setDestination(createEmailRequest.getDestination());
            emailAllocation.setEmailId(createEmailRequest.getEmailId());
            emailAllocation.setSource(createEmailRequest.getSource());

            Random random = new Random();
            emailAllocation.setForwardsTo(
                    emailServerConfig.getTargetAlias().get(random.nextInt(emailServerConfig.getTargetAlias().size()))
            );
            emailServerInteraction.createEmailId(emailAllocation);

            emailResponse.setCreated(true);
            emailResponse.setMessage("Email Address created");

            return new ResponseEntity<>(emailResponse, HttpStatus.CREATED);

        }catch(Exception execption){
            emailResponse.setMessage(execption.getMessage());
            emailResponse.setCreated(false);
        }

        return new ResponseEntity<>(emailResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/stats")
    public TrashemailStats presentDashBoard(){
        TrashemailStats trashemailStats = new TrashemailStats();
//        trashemailStats.setNumberOfUsers(
//                userRepository.getDistinctChatIdCount()
//        );
//        trashemailStats.setNumberOfEmailsRegistered(
//                userRepository.count()
//        );
//
//
//        Map<String, Long> domainCount = new HashMap<String, Long>();
//        for(String domain: emailServerConfig.getHosts()){
//            Long countForDomain = (
//                    (Integer) userRepository.findByEmailIdEndsWith(
//                        domain
//                    ).size()
//            ).longValue();
//
//            domainCount.put(domain, countForDomain);
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        Date today = calendar.getTime();
//
//        calendar.add(Calendar.DAY_OF_MONTH, -7);
//        Date sevenDaysBefore = calendar.getTime();
//
//        trashemailStats.setEmailIdsCreatedToday(
//                userRepository.getEmailIdsCreatedTodayCount()
//        );
//
//        trashemailStats.setEmailIdsCreatedInWeek(
//                userRepository.getEmailIdsCreatedInWeek(
//                        today,
//                        sevenDaysBefore
//                )
//        );
//        trashemailStats.setDomainsToNumbers(domainCount);
//        trashemailStats.setVersion(
//                trashemailConfig.getVersion()
//        );
//        trashemailStats.setNumberOfEmailsProcessed(
//            emailCounterRepository.count()
//        );
//        trashemailStats.setTotalNumberOfUsers(
//                userRepository.getNumberOfUsers()
//        );
//        trashemailStats.setCummulativeEmailsCountPerDay(
//                userRepository.getCommulativeEmailCounts(PageRequest.of(0, 2))
//        );
        return trashemailStats;
    }
}
