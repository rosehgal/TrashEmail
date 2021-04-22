package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.exceptions.EmailAliasNotCreatedExecption;
import io.github.trashemail.models.EmailAllocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServerInteractionTests {

    @InjectMocks
    private EmailServerInteraction emailServerInteraction;
    @Mock
    private EmailServerConfig emailServerConfig;
    @Mock
    private RestTemplate restTemplate;

    private static EmailAllocation emailAllocation;

    @BeforeAll
    static void setUpBeforeClass() {
        emailAllocation = new EmailAllocation();
        emailAllocation.setEmailId("a@a.com");
        emailAllocation.setEmailId("emailid@a.com");
        emailAllocation.setForwardsTo("forwardsto@a.com");
    }

    @BeforeEach
    void setUpBefore() {
        when(emailServerConfig.getAdminEmail()).thenReturn("admin@a.com");
        when(emailServerConfig.getAdminPassword()).thenReturn("password");

    }

    @AfterEach
    void teardown() {
        emailServerConfig = null;
        restTemplate = null;
    }


    @Test
    void testCreateEmail() throws EmailAliasNotCreatedExecption {
        when(emailServerConfig.getAddUrl()).thenReturn("www.addurl.com");
        when(restTemplate.postForEntity(Mockito.anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(""));

        String actual = emailServerInteraction.createEmailId(emailAllocation);

        assertEquals("Email ID : *emailid@a.com* successfully Created :)", actual);
    }

    @Test
    void testCreateEmailException() throws EmailAliasNotCreatedExecption {
        when(emailServerConfig.getAddUrl()).thenReturn("www.addurl.com");
        when(restTemplate.postForEntity(Mockito.anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(ResponseEntity.badRequest().body(""));

        assertThrows(EmailAliasNotCreatedExecption.class, () -> emailServerInteraction.createEmailId(emailAllocation));
    }


    @Test
    @DisplayName("Delete email with 200 response")
    void testDeleteEmailId() {
        when(emailServerConfig.getRemoveUrl()).thenReturn("www.removeurl.com");
        when(restTemplate.postForEntity(Mockito.anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(""));

        boolean actual = emailServerInteraction.deleteEmailId(emailAllocation);

        assertTrue(actual);
    }

    @Test
    @DisplayName("Delete email with 400 response")
    void testDeleteEmailIdBadRequest() {
        when(emailServerConfig.getRemoveUrl()).thenReturn("www.removeurl.com");
        when(restTemplate.postForEntity(Mockito.anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(HttpStatus.BAD_REQUEST));

        boolean actual = emailServerInteraction.deleteEmailId(emailAllocation);

        assertFalse(actual);
    }

    @Test
    @DisplayName("Delete email with 500 response")
    void testDeleteEmailIdBadRequestOtherStatusCode() {
        when(emailServerConfig.getRemoveUrl()).thenReturn("www.removeurl.com");
        when(restTemplate.postForEntity(Mockito.anyString(), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));

        boolean actual = emailServerInteraction.deleteEmailId(emailAllocation);

        assertFalse(actual);
    }
}
