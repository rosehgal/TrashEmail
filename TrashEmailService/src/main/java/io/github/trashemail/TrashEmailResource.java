package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.Configurations.TrashEmailConfig;
import io.github.trashemail.DTO.*;
import io.github.trashemail.exceptions.EmailAlreadyExsitExecption;
import io.github.trashemail.exceptions.EmailNotFoundExecption;
import io.github.trashemail.models.EmailAllocation;
import io.github.trashemail.repositories.EmailAllocationRepository;
import io.github.trashemail.repositories.EmailCounterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;


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

    @Autowired
    RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            TrashEmailResource.class);

    @PostMapping(value = "/create")
    public ResponseEntity<CreateEmailResponse> createEmailId(@RequestBody CreateEmailRequest createEmailRequest){
        CreateEmailResponse createEmailResponse = new CreateEmailResponse();
        try{
            EmailAllocation checkIfExist = emailAllocationRepository.findByEmailIdAndIsActiveTrue(
                    createEmailRequest.getEmailId());
            if(checkIfExist != null)
                throw new EmailAlreadyExsitExecption();

            EmailAllocation emailAllocation = new EmailAllocation(createEmailRequest);
            EmailAllocation existOlder = emailAllocationRepository.findByEmailIdAndDestinationAndDestinationType(
              emailAllocation.getEmailId(),
              emailAllocation.getDestination(),
              emailAllocation.getDestinationType()
            );
            if (existOlder != null)
                emailAllocation = existOlder;
            else {
                Random random = new Random();
                emailAllocation.setForwardsTo(
                    emailServerConfig
                        .getTargetAlias()
                        .get(random.nextInt(emailServerConfig.getTargetAlias().size())));
                }
            emailAllocation.setIsActive(true);

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

    @PostMapping(value = "/sendMail")
    public String sendMail(@RequestBody SendEmailRequest sendEmailRequest){
        emailCounterRepository.updateCount();

        EmailAllocation emailAllocation = emailAllocationRepository.findByEmailIdAndIsActiveTrue(
                sendEmailRequest.getEmailId());
        if(emailAllocation == null){
            return "Mail target not active";
        }

        String mailTargetType = emailAllocation.getDestinationType();

        if(mailTargetType.equals("url") || mailTargetType.equals("telegram")){
            /*
            Create a post request
            */
            String targetURI = emailAllocation.getDestination();
            Email email = new Email(sendEmailRequest);

            ResponseEntity response = restTemplate.postForEntity(
                    targetURI,
                    email,
                    String.class
            );
            return (String) response.getBody();

        }
        else if(mailTargetType.equals("email")){
            /*
            Send Email
            */
        }

        return "Mail Sent.";
    }

    @GetMapping(value = "/stats")
    public TrashEmailStats presentDashBoard(){
        TrashEmailStats trashemailStats = new TrashEmailStats();
        trashemailStats.setVersion(trashemailConfig.getVersion());
        List<ConnectorStats> connectorStats = new ArrayList<>();

        /*
        Get Stats from each connector
        */
        for(String connectorURL : trashemailConfig.getConnectorURLs()){
            connectorStats.add(
                    restTemplate.getForEntity(connectorURL + "/stats/", ConnectorStats.class).getBody()
            );
        }
        trashemailStats.setConnectorStats(connectorStats);

        Map<String, Long> domainCount = new HashMap<String, Long>();
        for(String domain: emailServerConfig.getHosts()){
            Long countForDomain = (
                    (Integer) emailAllocationRepository.findByEmailIdEndsWith(domain).size()
            ).longValue();

            domainCount.put(domain, countForDomain);
        }
        trashemailStats.setDomainsToNumbers(domainCount);
        trashemailStats.setEmailIdsCreatedToday(emailAllocationRepository.getEmailIdsCreatedTodayCount());

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysBefore = calendar.getTime();

        trashemailStats.setEmailIdsCreatedInWeek(
                emailAllocationRepository.getEmailIdsCreatedInWeek(
                        today,
                        sevenDaysBefore
                )
        );

        trashemailStats.setNumberOfEmailsProcessed(
            emailCounterRepository.count()
        );

        return trashemailStats;
    }
}
