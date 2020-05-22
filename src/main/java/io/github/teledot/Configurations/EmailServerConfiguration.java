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
	
	@Value("${emailServer.api.aliases.url}")
	private String emailServerApiAliasesUrl;

	@Value("${emailServer.aliases.target}")
	private String emailServerTargetAlias;

	public String getEmailServerTargetAlias() {
		return emailServerTargetAlias;
	}

	public void setEmailServerTargetAlias(String emailServerTargetAlias) {
		this.emailServerTargetAlias = emailServerTargetAlias;
	}

	public String getAdminemail() {
		return adminemail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getEmailServerApiAliasesUrl() {
		return emailServerApiAliasesUrl;
	}

	public void setEmailServerApiAliasesUrl(String emailServerApiAliasesUrl) {
		this.emailServerApiAliasesUrl = emailServerApiAliasesUrl;
	}

	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
}	
