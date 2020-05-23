package io.github.teledot.Telegram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.teledot.Configurations.EmailServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import io.github.teledot.EmailInteraction.EmailServerInteraction;
import io.github.teledot.Respositories.UserRepository;
import io.github.teledot.models.User;

@Component
public class TelegramRequestHandler {
	
	@Autowired
	private EmailServerInteraction emailServerInteraction;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailServerConfiguration emailServerConfiguration;
	
	public String createEmailHandler(User user) throws HttpClientErrorException {
		return emailServerInteraction.createEmailId(user); 
	}

	public String handleRequest(Integer chatId, String text) {
		String validTelegramMessageRegex = "(/(\\w+)) ([a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$)";
		
		Pattern pattern = Pattern.compile(validTelegramMessageRegex);
		Matcher matcher = pattern.matcher(text);
		
		if (matcher.matches()) {
			String command = matcher.group(2);
			String input = matcher.group(3);
			
			System.out.println(command);
			System.out.println(input);
			
			if(command.equals("create")) {
				if(userRepository.findByChatId(chatId).size() >=2) {
					return "Only two emails are allowed for User";
				}

				String emailId = input;
				
				if(userRepository.findByEmailId(emailId).size()==1)
					return "Email id is taken, please choose something else";
				
				User user = new User(chatId, emailId, emailServerConfiguration.getEmailServerTargetAlias());
				userRepository.save(user);
				
				return this.createEmailHandler(user);
			}
			else {
				return "I dont understand";
			}
		}
		else {
			return "Unable to parse message";
		}
	}

}
