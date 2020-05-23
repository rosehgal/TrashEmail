package io.github.teledot.Telegram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.teledot.Configurations.EmailServerConfiguration;
import io.github.teledot.EmailInteraction.ImapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(TelegramRequestHandler.class);
	
	public String createEmail(User user) throws HttpClientErrorException {
		return emailServerInteraction.createEmailId(user); 
	}

	public String handleRequest(Integer chatId, String text) {

		String []strings = text.split(" ");

		String command 	= "";
		String argument = "";

		if(strings.length >= 1)
			command = strings[0];
		if(strings.length >= 2)
			argument = strings[1];

		log.debug(command);
		log.debug(argument);

		switch(command){
			case "/create":
				if(userRepository.findByChatId(chatId).size()>=2)
					return "Only Two email Ids are allowed per-user\n" +
							"You can get the list of all the emails @ /get_all_emails";

				else{
					// parse the argument and treat it as email id.
					String emailRegex = "^[A-Za-z0-9._%+-]+@"+
							emailServerConfiguration.getEmailServerhost()+
							"$";

					log.debug(emailRegex);
					Pattern pattern = Pattern.compile(emailRegex);
					Matcher matcher = pattern.matcher(argument);

					// A valid domain email is inputted by the user
					if (matcher.matches()) {
						String emailId = argument;
						if(userRepository.existsByEmailId(emailId)){
							// Email ID Already taken
							return "Email ID " + argument + "" +
									"is already taken, please get some other";
						}

						User user = new User(chatId,
								   			 emailId,
											 emailServerConfiguration.getEmailServerTargetAlias());
						userRepository.save(user);
						return this.createEmail(user);
					}
					else{
						return "Email id should be of the form: *@"+
								emailServerConfiguration.getEmailServerhost();
					}
				}
			case "/help":
				break;
			case "/get_all_mails":
				break;
			case "/delete":
				break;
			default:
				return "I dont understand that ...";
		}

		return null;
//		String validTelegramMessageRegex = "(/(\\w+)) ([a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$)";
//
//		Pattern pattern = Pattern.compile(validTelegramMessageRegex);
//		Matcher matcher = pattern.matcher(text);
//
//		if (matcher.matches()) {
//			String command = matcher.group(2);
//			String input = matcher.group(3);
//
//			System.out.println(command);
//			System.out.println(input);
//
//			if(command.equals("create")) {
//				if(userRepository.findByChatId(chatId).size() >=2) {
//					return "Only two emails are allowed for User";
//				}
//
//				String emailId = input;
//
//				if(userRepository.findByEmailId(emailId).size()==1)
//					return "Email id is taken, please choose something else";
//
//				User user = new User(chatId, emailId, emailServerConfiguration.getEmailServerTargetAlias());
//				userRepository.save(user);
//
//				return this.createEmailHandler(user);
//			}
//			else {
//				return "I dont understand";
//			}
//		}
//		else {
//			return "Unable to parse message";
//		}
	}

}
