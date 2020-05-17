package io.github.teledot.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("EmailServerConfig")
public class EmailServerConfiguration {
	@Value("${emailServer.admin.email}")
	private String adminemail;
	
	@Value("${emailServer.admin.password}")
	private String adminPassword;
	
	@Value("${emailServer.apiUrl}")
	private String emailServerApiURL;

	public String getAdminemail() {
		return adminemail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getEmailServerApiURL() {
		return emailServerApiURL;
	}

	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public void setEmailServerApiURL(String emailServerApiURL) {
		this.emailServerApiURL = emailServerApiURL;
	}
		
}	
