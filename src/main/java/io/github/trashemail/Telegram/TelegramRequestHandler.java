package io.github.trashemail.Telegram;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.trashemail.Configurations.EmailServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import io.github.trashemail.EmailInteraction.EmailServerInteraction;
import io.github.trashemail.Respositories.UserRepository;
import io.github.trashemail.models.User;

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
			return "Email Id *deleted* and added to open pool.";
		}
		return "No mail ID on the server was identified with" +
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

		switch(command){
			case "/start":
				return "Thanks for joining ...\n" +
						"I am a disposable bot email id generator that can:\n" +
						"1. Quickly generate a disposable email for you, no email management hassle \uD83D\uDE0B\t\n" +
						"2. Quickly delete those emailIds\n" +
						"3. Since I own, trashemail.in your emailId will belong this domain\n" +
						"4. I dont read anything, I respect your privacy. Any doubt? Audit my source code." +
						"\n\n" +
						"I am still a baby and learning.\n" +
						"Do you have idea for addon? Please feel free to reach @ https://github.com/r0hi7/Trashemail" +
						" and raise an issue. I will definitely work out on that" +
						"\n\n" +
						"For now to get started try\n" +
						"* /create <user>@trashemail.in\n" +
						"* /delete <user>@trashemail.in\n" +
						"* /help\n" +
						"* /emails\n";

			case "/create":
				if(userRepository.findByChatId(chatId).size()>=2)
					return "Only Two email Ids are allowed per-user\n" +
							"You can get the list of all the emails @ /emails";

				else{
					// parse the argument and treat it as email id.
					String emailRegex = "^[A-Za-z0-9._%+-]+@"+
							emailServerConfiguration.getEmailServerhost()+
							"$";

					Pattern pattern = Pattern.compile(emailRegex);
					Matcher matcher = pattern.matcher(argument);

					// A valid domain email is inputted by the user
					if (matcher.matches()) {
						String emailId = argument;
						if(userRepository.existsByEmailId(emailId)){
							// Email ID Already taken
							return "Email ID *" + argument + "* " +
									"is already taken, please get some other";
						}

						User user = new User(chatId,
								   			 emailId,
											 emailServerConfiguration.getEmailServerImapTaregtUsername());
						userRepository.save(user);
						return this.createEmail(user);
					}
					else{
						return "Email id should be of the form: `*@"+
								emailServerConfiguration.getEmailServerhost()+"`";
					}
				}
			case "/help":
				return "Create disposable email addresses to protect you against spam and newsletters." +
						"E-Mail forwarding made easy.\n" +
						"I am open source and runs on open source services." +
						"You can find my heart, soul and brain at: https://github.com/r0hi7/Trashemail\n\n"+
						"Currently, I support:\n" +
						"/create - That can get you one(or two) custom disposable emails.\n" +
						"/delete - If you are getting spams for any mail id, just delete it because it is disposable.\n" +
						"/emails - Get the list of all the emailIds that belongs to you.\n" +
						"/help - this help message.\n";

			case "/emails":
				String response = "Currently, you have below mentioned emails with you.\n*";
				List<User> allEmailsWithUser = userRepository.findByChatId(chatId);
				for(User emailWithUser: allEmailsWithUser){
					response += emailWithUser + "\n";
				}
				response+="*";
				return response;

			case "/delete":
				String emailRegex = "^[A-Za-z0-9._%+-]+@"+
						emailServerConfiguration.getEmailServerhost()+
						"$";

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
						return "*Email not registered to any user ..*";
					}

				}
				else{
					return "Email id should be of the form: `*@"+
							emailServerConfiguration.getEmailServerhost()+'`';
				}
				break;

			default:
				return "I dont understand that ...\n " +
						"I only understand few commands.\n" +
						"/create <something>@trashemail.in\n" +
						"/delete <something>@trashemail.in\n" +
						"/emails\n" +
						"/help\n";
		}

		return null;
	}

}
