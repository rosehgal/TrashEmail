package io.github.trashemail.EmailInteraction;

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

	
	public String createEmailId(User user) throws HttpClientErrorException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
			emailServerConfig.getAdminemail(),
			emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		
		data.add("address", user.getEmailId());
		data.add("forwards_to", emailServerConfig.getEmailServerTargetAlias());
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(data, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response = restTemplate.postForEntity(
			emailServerConfig.getEmailServerApiAliasesUrl(),
			request, 
			String.class);

		if(response.getStatusCode() == HttpStatus.OK){
			return "Email ID : *"+user.getEmailId()+"* "+
					"successfully Created :)";
		}

		if(response.getStatusCode() == HttpStatus.BAD_REQUEST &&
				((String)response.getBody()).contains("Alias already exists")){
			return "Email ID : *"+user.getEmailId()+"* "+
					"already taken :)";
		}

		return response.getBody().toString();
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
