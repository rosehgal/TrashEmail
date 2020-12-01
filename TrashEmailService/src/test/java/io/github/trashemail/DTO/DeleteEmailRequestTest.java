package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteEmailRequestTest {
	private DeleteEmailRequest deleteEmailRequest;
	
	@BeforeEach
	public void setUp() {
		deleteEmailRequest = new DeleteEmailRequest();
	}
	
	@Test
	public void getterNullTest() {
		assertNull(deleteEmailRequest.getEmailId());
	}
	
	@Test
	public void setterTest() {
		String emailId = "emailId";
		deleteEmailRequest.setEmailId(emailId);
		assertEquals(emailId, deleteEmailRequest.getEmailId());
	}
}