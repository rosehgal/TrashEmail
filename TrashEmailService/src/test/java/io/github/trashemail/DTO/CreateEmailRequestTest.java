package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateEmailRequestTest {
	private CreateEmailRequest createEmailRequest;

	@BeforeEach
	public void setUp() {
		createEmailRequest = new CreateEmailRequest();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(createEmailRequest.getSource());
		assertNull(createEmailRequest.getDestination());
		assertNull(createEmailRequest.getDestinationType());
		assertNull(createEmailRequest.getEmailId());
		assertNull(createEmailRequest.getIsActive());
	}
	
	@Test
	public void settersTest() {
		String source = "source";
		createEmailRequest.setSource(source);
		assertEquals(source, createEmailRequest.getSource());
		
		String destination = "destination";
		createEmailRequest.setDestination(destination);
		assertEquals(destination, createEmailRequest.getDestination());
		
		String destinationType = "destinationType";
	    createEmailRequest.setDestinationType(destinationType);
		assertEquals(destinationType, createEmailRequest.getDestinationType());
		
		String emailId = "emailId";
	    createEmailRequest.setEmailId(emailId);
		assertEquals(emailId, createEmailRequest.getEmailId());
		
	    boolean isActive = true;
	    createEmailRequest.setIsActive(isActive);
		assertEquals(isActive, createEmailRequest.getIsActive());
	}
}