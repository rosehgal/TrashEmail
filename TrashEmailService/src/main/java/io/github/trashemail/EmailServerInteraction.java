package io.github.trashemail;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.exceptions.EmailAliasNotCreatedExecption;
import io.github.trashemail.models.EmailAllocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component("EmailServerInteraction")
public class EmailServerInteraction {

	@Autowired
	private EmailServerConfig emailServerConfig;

	@Autowired
	RestTemplate restTemplate;

	private static final Logger log = LoggerFactory.getLogger(
			EmailServerInteraction.class);

	public String createEmailId(EmailAllocation emailAllocation)
	throws HttpClientErrorException, EmailAliasNotCreatedExecption {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
			emailServerConfig.getAdminEmail(),
			emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		
		data.add("address", emailAllocation.getEmailId());
		data.add("forwards_to", emailAllocation.getForwardsTo());
		
		HttpEntity<MultiValueMap<String, String>> request =
				new HttpEntity<MultiValueMap<String, String>>(data, headers);

		ResponseEntity response = restTemplate.postForEntity(
			emailServerConfig.getAddUrl(),
			request, 
			String.class);

		if(response.getStatusCode() == HttpStatus.OK){
			return "Email ID : *"+emailAllocation.getEmailId()+"* "+
					"successfully Created :)";
		}

		log.error(response.getStatusCode().toString() + response.getBody());
		throw new EmailAliasNotCreatedExecption(response.getBody().toString());
	}

	public boolean deleteEmailId(EmailAllocation emailAllocation) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
				emailServerConfig.getAdminEmail(),
				emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();

		data.add("address", emailAllocation.getEmailId());

		HttpEntity<MultiValueMap<String, String>> request =
				new HttpEntity<MultiValueMap<String, String>>(data, headers);

//		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response = restTemplate.postForEntity(
				emailServerConfig.getRemoveUrl(),
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
