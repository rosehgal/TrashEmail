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
		// TODO create user model here and check if email id exists or not
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(
			emailServerConfig.getAdminemail(),
			emailServerConfig.getAdminPassword()
		);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		
		map.add("email", user.getEmailId());
		map.add("password", user.getPassword());
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForEntity(
			emailServerConfig.getEmailServerApiURL(), 
			request, 
			String.class).getBody();
		
		return response;
	}
	
	public Object fetchEmail(String emailId) {
		return null;
	}

    public static class GetEmailsAndSendTelegramMessages {
    }
}
