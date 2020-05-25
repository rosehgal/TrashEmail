package io.github.trashemail.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("EmailServerConfig")
public class EmailServerConfiguration {
	@Value("${emailServer.admin.email}")
	private String adminemail;
	
	@Value("${emailServer.admin.password}")
	private String adminPassword;
	
	@Value("${emailServer.api.aliases.add_url}")
	private String emailServerApiAddAliasesUrl;

	@Value("${emailServer.imap.email}")
	private String emailServerImapTaregtUsername;

	@Value("${emailServer.imap.password}")
	private String emailServerImapTargetPassword;

	@Value("${emailServer.imap.port}")
	private String emailServerImapPort;

	@Value("${emailServer.imap.host}")
	private String emailServerImapHost;

	@Value("${emailServer.host}")
	private String emailServerhost;

	@Value("${emailServer.api.aliases.remove_url}")
	private String emailServerApiRemoveAliasUrl;

	public String getEmailServerApiRemoveAliasUrl() {
		return emailServerApiRemoveAliasUrl;
	}

	public void setEmailServerApiRemoveAliasUrl(String emailServerApiRemoveAliasUrl) {
		this.emailServerApiRemoveAliasUrl = emailServerApiRemoveAliasUrl;
	}

	public String getEmailServerhost() {
		return emailServerhost;
	}

	public void setEmailServerhost(String emailServerhost) {
		this.emailServerhost = emailServerhost;
	}

	public String getEmailServerImapHost() {
		return emailServerImapHost;
	}

	public void setEmailServerImapHost(String emailServerImapHost) {
		this.emailServerImapHost = emailServerImapHost;
	}

	public String getEmailServerImapPort() {
		return emailServerImapPort;
	}

	public void setEmailServerImapPort(String emailServerImapPort) {
		this.emailServerImapPort = emailServerImapPort;
	}


	public String getEmailServerImapTargetPassword() {
		return emailServerImapTargetPassword;
	}

	public void setEmailServerImapTargetPassword(String emailServerImapTargetPassword) {
		this.emailServerImapTargetPassword = emailServerImapTargetPassword;
	}

	public String getEmailServerImapTaregtUsername() {
		return emailServerImapTaregtUsername;
	}

	public void setEmailServerImapTaregtUsername(String emailServerImapTaregtUsername) {
		this.emailServerImapTaregtUsername = emailServerImapTaregtUsername;
	}

	public String getAdminemail() {
		return adminemail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getEmailServerApiAddAliasesUrl() {
		return emailServerApiAddAliasesUrl;
	}

	public void setEmailServerApiAddAliasesUrl(String emailServerApiAddAliasesUrl) {
		this.emailServerApiAddAliasesUrl = emailServerApiAddAliasesUrl;
	}

	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
}
