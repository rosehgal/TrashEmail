package io.github.teledot.EmailInteraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.teledot.Configurations.EmailServerConfiguration;
import io.github.teledot.models.User;


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
		String response = restTemplate.postForEntity(
			emailServerConfig.getEmailServerApiAliasesUrl(),
			request, 
			String.class).getBody();
		
		return response;
	}
}
