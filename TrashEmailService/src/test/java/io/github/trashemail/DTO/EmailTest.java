package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class EmailTest {
	private Email email;
	private SendEmailRequest sendEmailRequest;

	@BeforeEach
	public void setUp() {
		sendEmailRequest = mock(SendEmailRequest.class);
		email = new Email(sendEmailRequest);
	}

	@Test
	public void constructorTest() {
		assertNotNull(email.getArrived());
		assertEquals(email.getEmailId(), sendEmailRequest.getEmailId());
		assertEquals(email.getMessage(), sendEmailRequest.getMessage());
		assertEquals(email.getEmailURI(), sendEmailRequest.getEmailURI());
		assertEquals(email.getEmailDownloadPath(), sendEmailRequest.getEmailDownloadPath());
		assertEquals(email.getAttachmentsPaths(), sendEmailRequest.getAttachmentsPaths());
	}
} 