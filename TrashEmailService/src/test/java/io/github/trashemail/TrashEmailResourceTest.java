package io.github.trashemail;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.Configurations.TrashEmailConfig;
import io.github.trashemail.DTO.ConnectorStats;
import io.github.trashemail.DTO.CreateEmailRequest;
import io.github.trashemail.DTO.DeleteEmailRequest;
import io.github.trashemail.DTO.SendEmailRequest;
import io.github.trashemail.models.EmailAllocation;
import io.github.trashemail.repositories.EmailAllocationRepository;
import io.github.trashemail.repositories.EmailCounterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = TrashEmailResource.class)
class TrashEmailResourceTest {

    @MockBean
    private EmailAllocationRepository emailAllocationRepository;
    @MockBean
    private EmailCounterRepository emailCounterRepository;
    @MockBean
    private EmailServerConfig emailServerConfig;
    @MockBean
    private EmailServerInteraction emailServerInteraction;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private TrashEmailConfig trashEmailConfig;

    @Test
    void testPresentDashBoard() throws Exception {

        ConnectorStats connectorStats = new ConnectorStats();
        connectorStats.setConnectorName("connectorname");


        when(trashEmailConfig.getVersion()).thenReturn("1.0.0");
        when(trashEmailConfig.getConnectorURLs()).thenReturn(Arrays.asList("www.connection.com"));
        when(emailServerConfig.getHosts()).thenReturn(Arrays.asList("localhost"));
        when(emailAllocationRepository.findByEmailIdEndsWith(anyString())).thenReturn(Arrays.asList(new EmailAllocation()));
        when(emailAllocationRepository.getEmailIdsCreatedTodayCount()).thenReturn(1L);
        when(emailAllocationRepository.getEmailIdsCreatedInWeek(any(Date.class), any(Date.class))).thenReturn(Arrays.asList(1L));
        when(emailAllocationRepository.count()).thenReturn(1L);

        when(restTemplate.getForEntity(anyString(), any(Class.class)))
                .thenReturn(new ResponseEntity(connectorStats, HttpStatus.OK));

        mockMvc.perform(get("/stats"))
               .andDo(print())
               .andExpect(content().json("{\"numberOfUsers\":null,\"numberOfEmailsRegistered\":null,\"domainsToNumbers\":{\"localhost\":1},\"emailIdsCreatedToday\":1,\"emailIdsCreatedInWeek\":[1],\"version\":\"1.0.0\",\"numberOfEmailsProcessed\":0,\"totalNumberOfUsers\":null,\"connectorStats\":[{\"connectorName\":\"connectorname\",\"activeUsers\":null,\"totalNumberOfUsers\":null,\"activeEmailIds\":null,\"totalNumberOfEmailIds\":null}]}"))
               .andExpect(status().isOk());
    }


    @Test
    void testSendEmailTargetNotActive() throws Exception {

        doNothing().when(emailCounterRepository).updateCount();
        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(null);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setEmailId("emailid");
        String payload = new ObjectMapper().writeValueAsString(sendEmailRequest);

        mockMvc.perform(post("/sendMail")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Mail target not active"));
    }


    @Test
    void testSendEmail() throws Exception {

        doNothing().when(emailCounterRepository).updateCount();

        EmailAllocation emailAllocation = new EmailAllocation();
        emailAllocation.setDestinationType("url");
        emailAllocation.setDestination("destination");

        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(emailAllocation);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setEmailId("emailid");
        String payload = new ObjectMapper().writeValueAsString(sendEmailRequest);

        when(restTemplate.postForEntity(anyString(), any(Object.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(""));


        mockMvc.perform(post("/sendMail")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    void testSendEmailTargetTypeEmail() throws Exception {

        doNothing().when(emailCounterRepository).updateCount();

        EmailAllocation emailAllocation = new EmailAllocation();
        emailAllocation.setDestinationType("email");
        emailAllocation.setDestination("destination");

        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(emailAllocation);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setEmailId("emailid");
        String payload = new ObjectMapper().writeValueAsString(sendEmailRequest);

        when(restTemplate.postForEntity(anyString(), any(Object.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(""));


        mockMvc.perform(post("/sendMail")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string("Mail Sent."));
    }

    @Test
    void testDeleteEmailIdBadRequest() throws Exception {
        DeleteEmailRequest deleteEmailRequest = new DeleteEmailRequest();
        deleteEmailRequest.setEmailId("emailId");
        String payload = new ObjectMapper().writeValueAsString(deleteEmailRequest);

        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(null);

        mockMvc.perform(post("/delete")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(payload))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEmailId() throws Exception {
        DeleteEmailRequest deleteEmailRequest = new DeleteEmailRequest();
        deleteEmailRequest.setEmailId("emailId");
        String payload = new ObjectMapper().writeValueAsString(deleteEmailRequest);
        EmailAllocation emailAllocation = new EmailAllocation();


        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(emailAllocation);
        when(emailServerInteraction.deleteEmailId(any(EmailAllocation.class))).thenReturn(true);
        when(emailAllocationRepository.save(any(EmailAllocation.class))).thenReturn(emailAllocation);

        mockMvc.perform(post("/delete")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().json("{\"emailId\":\"emailId\",\"isDeleted\":true}"));
    }

    @Test
    void testCreateEmailBadRequest() throws Exception {
        CreateEmailRequest createEmailRequest = new CreateEmailRequest();
        createEmailRequest.setEmailId("emailid");
        String payload = new ObjectMapper().writeValueAsString(createEmailRequest);
        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(new EmailAllocation());

        mockMvc.perform(post("/create")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(payload))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEmailCreated() throws Exception {
        CreateEmailRequest createEmailRequest = new CreateEmailRequest();
        createEmailRequest.setEmailId("emailid");
        createEmailRequest.setDestination("destination");
        createEmailRequest.setDestinationType("destinationtype");
        EmailAllocation emailAllocation = new EmailAllocation();
        String payload = new ObjectMapper().writeValueAsString(createEmailRequest);
        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(null);
        when(emailAllocationRepository.findByEmailIdAndDestinationAndDestinationType(anyString(),
                                                                                     anyString(),
                                                                                     anyString())).thenReturn(emailAllocation);

        when(emailServerInteraction.createEmailId(any(EmailAllocation.class))).thenReturn("");
        when(emailAllocationRepository.save(any(EmailAllocation.class))).thenReturn(emailAllocation);

        mockMvc.perform(post("/create")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    void testCreateEmailRandom() throws Exception {
        CreateEmailRequest createEmailRequest = new CreateEmailRequest();
        createEmailRequest.setEmailId("emailid");
        createEmailRequest.setDestination("destination");
        createEmailRequest.setDestinationType("destinationtype");
        EmailAllocation emailAllocation = new EmailAllocation();
        String payload = new ObjectMapper().writeValueAsString(createEmailRequest);
        when(emailAllocationRepository.findByEmailIdAndIsActiveTrue(anyString())).thenReturn(null);
        when(emailAllocationRepository.findByEmailIdAndDestinationAndDestinationType(anyString(),
                                                                                     anyString(),
                                                                                     anyString())).thenReturn(null);

        when(emailServerConfig.getTargetAlias()).thenReturn(Arrays.asList("Alias"));
        when(emailServerInteraction.createEmailId(any(EmailAllocation.class))).thenReturn("");
        when(emailAllocationRepository.save(any(EmailAllocation.class))).thenReturn(emailAllocation);

        mockMvc.perform(post("/create")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(payload))
               .andDo(print())
               .andExpect(status().isCreated());
    }
}
