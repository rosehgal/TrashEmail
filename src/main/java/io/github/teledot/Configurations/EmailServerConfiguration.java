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

	@Value("${emailServer.aliases.target.password}")
	private String emailServerTargetAliasPassword;

	@Value("${emailServer.pop3.host}")
	private String emailServerPop3Host;

	@Value("${emailServer.pop3.port}")
	private int emailServerPop3Port;

	@Value("${emailServer.imap.port}")
	private String emailServerImapPort;

	@Value("${emailServer.imap.host}")
	private String emailServerImapHost;

	@Value("${emailServer.host}")
	private String emailServerhost;

	@Value("${emailServer.api.aliases.remove.url}")
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

	public String getEmailServerPop3Host() {
		return emailServerPop3Host;
	}

	public void setEmailServerPop3Host(String emailServerPop3Host) {
		this.emailServerPop3Host = emailServerPop3Host;
	}

	public int getEmailServerPop3Port() {
		return emailServerPop3Port;
	}

	public void setEmailServerPop3Port(int emailServerPop3Port) {
		this.emailServerPop3Port = emailServerPop3Port;
	}

	public String getEmailServerTargetAliasPassword() {
		return emailServerTargetAliasPassword;
	}

	public void setEmailServerTargetAliasPassword(String emailServerTargetAliasPassword) {
		this.emailServerTargetAliasPassword = emailServerTargetAliasPassword;
	}

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
