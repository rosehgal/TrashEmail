package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.Configurations.TrashEmailConfig;
import io.github.trashemail.DTO.*;
import io.github.trashemail.exceptions.EmailNotFoundExecption;
import io.github.trashemail.models.EmailAllocation;
import io.github.trashemail.repositories.EmailAllocationRepository;
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
public class TrashEmailResource {


    @Autowired
    EmailCounterRepository emailCounterRepository;

    @Autowired
    EmailAllocationRepository emailAllocationRepository;

    @Autowired
    EmailServerConfig emailServerConfig;

    @Autowired
    TrashEmailConfig trashemailConfig;

    @Autowired
    EmailServerInteraction emailServerInteraction;

    @PostMapping(value = "/create")
    public ResponseEntity<CreateEmailResponse> createEmailId(@RequestBody CreateEmailRequest createEmailRequest){
        EmailAllocation emailAllocation = new EmailAllocation(createEmailRequest);
        CreateEmailResponse createEmailResponse = new CreateEmailResponse();

        try{

            Random random = new Random();
            emailAllocation.setForwardsTo(
                    emailServerConfig.getTargetAlias().get(random.nextInt(emailServerConfig.getTargetAlias().size()))
            );

            emailServerInteraction.createEmailId(emailAllocation);

            createEmailResponse.setCreated(true);
            createEmailResponse.setMessage("Email Address created");

            emailAllocationRepository.save(emailAllocation);
            return new ResponseEntity<>(createEmailResponse, HttpStatus.CREATED);

        }catch(Exception exception){
            createEmailResponse.setMessage(exception.getMessage());
            createEmailResponse.setCreated(false);
        }

        return new ResponseEntity<>(createEmailResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<DeleteEmailResponse> deleteEmailId(@RequestBody DeleteEmailRequest deleteEmailRequest){
        EmailAllocation emailAllocation = emailAllocationRepository.findByEmailIdAndIsActiveTrue(
                deleteEmailRequest.getEmailId());
        DeleteEmailResponse deleteEmailResponse = new DeleteEmailResponse();
        try{
            if(emailAllocation == null)
                throw new EmailNotFoundExecption();

            emailServerInteraction.deleteEmailId(emailAllocation);
            emailAllocation.setIsActive(false);
            emailAllocationRepository.save(emailAllocation);

            deleteEmailResponse.setEmailId(deleteEmailRequest.getEmailId());
            deleteEmailResponse.setIsDeleted(true);
            return new ResponseEntity<>(deleteEmailResponse, HttpStatus.OK);

        }catch (Exception e){
            deleteEmailResponse.setIsDeleted(false);
            deleteEmailResponse.setError(e.getMessage());
        }

        return new ResponseEntity<>(deleteEmailResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/stats")
    public TrashEmailStats presentDashBoard(){
        TrashEmailStats trashemailStats = new TrashEmailStats();
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
