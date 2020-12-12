package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SendEmailRequestTest {
	private SendEmailRequest sendEmailRequest;
	
	@BeforeEach
	public void setUp() {
		sendEmailRequest = new SendEmailRequest();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(sendEmailRequest.getEmailId());
		assertNull(sendEmailRequest.getMessage());
		assertNull(sendEmailRequest.getEmailURI());
		assertNull(sendEmailRequest.getEmailDownloadPath());
		assertNull(sendEmailRequest.getAttachmentsPaths());
	}
	
	@Test
	public void settersTest() {
		String emailId = "emailId";
		sendEmailRequest.setEmailId(emailId);
		assertEquals(emailId, sendEmailRequest.getEmailId());
		
		String message = "message";
		sendEmailRequest.setMessage(message);
		assertEquals(message, sendEmailRequest.getMessage());
		
		String emailURI = "emailURI";
		sendEmailRequest.setEmailURI(emailURI);
		assertEquals(emailURI, sendEmailRequest.getEmailURI());
		
		String emailDownloadPath = "emailDownloadPath";
		sendEmailRequest.setEmailDownloadPath(emailDownloadPath);
		assertEquals(emailDownloadPath, sendEmailRequest.getEmailDownloadPath());
		
	    List<String> attachmentsPaths = Arrays.asList("att1", "att2");
	    sendEmailRequest.setAttachmentsPaths(attachmentsPaths);
		assertEquals(attachmentsPaths, sendEmailRequest.getAttachmentsPaths());
	}
	
	@Test
	public void toStringTest() {
		// Set up
		String emailId = "emailId";
		sendEmailRequest.setEmailId(emailId);
		
		String message = "message";
		sendEmailRequest.setMessage(message);
		
		String emailURI = "emailURI";
		sendEmailRequest.setEmailURI(emailURI);
		
		String emailDownloadPath = "emailDownloadPath";
		sendEmailRequest.setEmailDownloadPath(emailDownloadPath);
		
		List<String> attachmentsPaths = Arrays.asList("att1", "att2");
	    sendEmailRequest.setAttachmentsPaths(attachmentsPaths);
		
		String texto = "SendEmailRequest{" +
                "emailId='" + sendEmailRequest.getEmailId() + '\'' +
                ", message='" + sendEmailRequest.getMessage() + '\'' +
                ", emailURI='" + sendEmailRequest.getEmailURI() + '\'' +
                ", emailDownloadPath='" + sendEmailRequest.getEmailDownloadPath()+ '\'' +
                ", attachmentsPaths=" + sendEmailRequest.getAttachmentsPaths() +
                '}';
		
		String resultado = sendEmailRequest.toString();
		
		assertEquals(texto, resultado);
	}
}