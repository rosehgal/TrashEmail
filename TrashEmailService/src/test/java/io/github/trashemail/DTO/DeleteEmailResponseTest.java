package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteEmailResponseTest {
	private DeleteEmailResponse deleteEmailResponse;
	
	@BeforeEach
	public void setUp() {
		deleteEmailResponse = new DeleteEmailResponse();
	}
	
	@Test
	public void settersTest() {
		String emailId = "emailId";
		deleteEmailResponse.setEmailId(emailId);
		assertEquals(emailId, deleteEmailResponse.getEmailId());
		
	    Boolean isDeleted = true;
	    deleteEmailResponse.setIsDeleted(isDeleted);
	    assertEquals(isDeleted, deleteEmailResponse.getIsDeleted());
	    
	    String error = "error";
	    deleteEmailResponse.setError(error);
	    assertEquals(error, deleteEmailResponse.getError());
	}
}