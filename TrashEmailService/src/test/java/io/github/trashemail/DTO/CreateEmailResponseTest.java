package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateEmailResponseTest {
	private CreateEmailResponse createEmailResponse;
	
	@BeforeEach
	public void setUp() {
		createEmailResponse = new CreateEmailResponse();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(createEmailResponse.getCreated());
		assertNull(createEmailResponse.getMessage());
	}
	
	@Test
	public void settersTest() {
		boolean created = true;
		createEmailResponse.setCreated(created);
		assertEquals(created, createEmailResponse.getCreated());
		
		String message = "message";
		createEmailResponse.setMessage(message);
		assertEquals(message, createEmailResponse.getMessage());
	}
}