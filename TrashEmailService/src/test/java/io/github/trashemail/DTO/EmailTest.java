package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

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
	
	@Test
	public void settersTest() {
		Date date = mock(Date.class);
		email.setArrived(date);
		assertEquals(date, email.getArrived());
		
		String emailId = "hola@gmail.com";
		email.setEmailId(emailId);
		assertEquals(emailId, email.getEmailId());
		
		String message = "hola";
		email.setMessage(message);
		assertEquals(message, email.getMessage());
		
		String emailURI = "hola@gmail.com";
		email.setEmailURI(emailURI);
		assertEquals(emailURI, email.getEmailURI());
		
		String emailDownloadPath = "www.direccion.com";
		email.setEmailDownloadPath(emailDownloadPath);
		assertEquals(emailDownloadPath, email.getEmailDownloadPath());
		
		List<String> attachmentsPaths = Arrays.asList("at1", "at2", "at3");
		email.setAttachmentsPaths(attachmentsPaths);
		assertEquals(attachmentsPaths, email.getAttachmentsPaths());
	}
} 