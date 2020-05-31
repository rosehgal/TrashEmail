package io.github.trashemail.EmailInteraction;

import io.github.trashemail.Telegram.TelegramRequestHandler;
import io.github.trashemail.utils.exceptions.EmailAliasNotCreatedExecption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.trashemail.Configurations.EmailServerConfiguration;
import io.github.trashemail.models.User;


@Component("EmailServerInteraction")
public class EmailServerInteraction {

	@Autowired
	private EmailServerConfiguration emailServerConfig;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(EmailServerInteraction.class);

	public String createEmailId(User user) throws HttpClientErrorException, EmailAliasNotCreatedExecption {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
			emailServerConfig.getAdminemail(),
			emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		
		data.add("address", user.getEmailId());
		data.add("forwards_to", emailServerConfig.getEmailServerImapTaregtUsername());
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(data, headers);

		ResponseEntity response = restTemplate.postForEntity(
			emailServerConfig.getEmailServerApiAddAliasesUrl(),
			request, 
			String.class);

		if(response.getStatusCode() == HttpStatus.OK){
			return "Email ID : *"+user.getEmailId()+"* "+
					"successfully Created :)";
		}

		log.error(response.getStatusCode().toString() + response.getBody());
		throw new EmailAliasNotCreatedExecption(response.getBody().toString());
	}

	public boolean deleteEmailId(User user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
				emailServerConfig.getAdminemail(),
				emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();

		data.add("address", user.getEmailId());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(data, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response = restTemplate.postForEntity(
				emailServerConfig.getEmailServerApiRemoveAliasUrl(),
				request,
				String.class);

		if(response.getStatusCode() == HttpStatus.OK){
			return true;
		}

		else if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
			return false;
		}

		return false;
	}
}
