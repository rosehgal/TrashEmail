package io.github.teledot.Telegram;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.github.teledot.Configurations.EmailServerConfiguration;
import io.github.teledot.EmailInteraction.ImapClient;
import javassist.expr.Instanceof;
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
	public String deleteEmail(User user) throws HttpClientErrorException{
		Boolean isDeleted = emailServerInteraction.deleteEmailId(user);
		if(isDeleted){
			userRepository.delete(user);
			return "Email Id deleted and added to open pool.";
		}
		return "No mail ID on the server was identified with " +
				user.getEmailId();
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
			case "/start":
				return "Thanks for joining ...";
			case "/create":
				if(userRepository.findByChatId(chatId).size()>=2)
					return "Only Two email Ids are allowed per-user\n" +
							"You can get the list of all the emails @ /emailIds";

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
			case "/emailIds":
				String response = "Currently, you have below mentioned emails with you.\n";
				List<User> allEmailsWithUser = userRepository.findByChatId(chatId);
				for(User emailWithUser: allEmailsWithUser){
					response += emailWithUser + "\n";
				}
				return response;

			case "/delete":
				String emailRegex = "^[A-Za-z0-9._%+-]+@"+
						emailServerConfiguration.getEmailServerhost()+
						"$";

				log.debug(emailRegex);
				Pattern pattern = Pattern.compile(emailRegex);
				Matcher matcher = pattern.matcher(argument);

				// A valid domain email is inputted by the user
				if (matcher.matches()) {
					String emailId = argument;
					User user = userRepository.findByEmailId(emailId);
					// user should only delete email owned by user.
					if(userRepository.existsByEmailId(emailId)){
						if(chatId.equals(user.getChatId())) {
							return this.deleteEmail(user);
						}
					}
					else{
						return "Email not registered to any user ..";
					}

				}
				else{
					return "Email id should be of the form: *@"+
							emailServerConfiguration.getEmailServerhost();
				}
				break;
			default:
				return "I dont understand that ...";
		}

		return null;
	}

}
