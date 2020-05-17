package io.github.teledot.models;

import java.util.Random;

public class UserEmail {

	private String email;
	private String password;
	
	public UserEmail() {}
	public UserEmail(String email) {
		this.email = email;
		
		// Create Random password for each user
		// and add it to persistent DB to use while fetching.
		
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 20;
	    Random random = new Random();
	 
	    this.password = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
