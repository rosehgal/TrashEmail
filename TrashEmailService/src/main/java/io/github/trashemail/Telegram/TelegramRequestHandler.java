package io.github.trashemail.Telegram;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.trashemail.Configurations.EmailServerConfig;
import io.github.trashemail.Configurations.TrashemailConfig;
import io.github.trashemail.Telegram.DTO.TelegramResponse;
import io.github.trashemail.Telegram.DTO.messageEntities.InlineKeyboardButton;
import io.github.trashemail.Telegram.DTO.messageEntities.InlineKeyboardMarkup;
import io.github.trashemail.utils.exceptions.EmailAliasNotCreatedExecption;
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
	private EmailServerConfig emailServerConfig;
	@Autowired
	private TrashemailConfig trashemailConfig;

	private static final Logger log = LoggerFactory.getLogger(
			TelegramRequestHandler.class);
	
	public String createEmail(User user)
	throws HttpClientErrorException, EmailAliasNotCreatedExecption {
		return emailServerInteraction.createEmailId(user); 
	}
	public String deleteEmail(User user)
	throws HttpClientErrorException{
		Boolean isDeleted = emailServerInteraction.deleteEmailId(user);
		if(isDeleted){
			userRepository.delete(user);
			return "Email Id *deleted* and added to open pool.";
		}
		return "No mail ID on the server was identified with" +
				user.getEmailId();
	}

	public TelegramResponse handleRequest(long chatId, String text) {

		String []strings = text.split("\\s+");

		String command 	= null;
		String argument = null;
		String responseText = "";

		if(strings.length >= 1)
			command = strings[0];
		if(strings.length >= 2)
			argument = strings[1];

		switch(command){
			case "/start":
				responseText =  "Thanks for joining ...\n" +
						"I am a disposable bot email id generator that can:\n" +
						"1. Quickly generate a disposable email for you," +
						" no email management hassle \uD83D\uDE0B\t\n" +
						"2. Quickly delete those emailIds\n" +
						"3. Since I own, " +
						"*trashemail.in*, *humblemail.com* & *thromail.com* " +
						"your emailId will belong these domains\n" +
						"4. I dont read anything, I respect your privacy." +
						"Any doubt? Audit my source code." +
						"\n\n" +
						"I am still a baby and learning.\n" +
						"Do you have idea for addon? " +
						"Please feel free to reach @" +
						" https://github.com/r0hi7/Trashemail" +
						" and raise an issue. " +
						"I will definitely work out on that" +
						"\n\n" +
						"For now to get started try\n" +
						"* /create <user>@("+
						String.join("|", emailServerConfig.getHosts()) +")\n" +
						"* /delete emailId\n" +
						"* /help\n" +
						"* /emails\n";

				return new TelegramResponse(
						chatId,
						responseText
				);

			case "/create":
				if(userRepository.findByChatId(chatId).size() >= trashemailConfig.getMaxEmailsPerUser()) {
					responseText =  "Only " + trashemailConfig.getMaxEmailsPerUser() + " " +
							"email Ids are allowed per-user\n" +
							"You can get the list of all the emails @ /emails";
					return new TelegramResponse(
							chatId,
							responseText
						);
				}

				else{
					if(argument != null) {
							/*
							User entered something like : /create username.
							No offer him interactive response.
							*/
						String emailRegex = "^[A-Za-z0-9._%+-]+@(" +
								String.join("|",
											emailServerConfig.getHosts()) +")$";

						Pattern pattern = Pattern.compile(emailRegex);
						Matcher matcher = pattern.matcher(argument);

						// A valid domain email is inputted by the user
						if (matcher.matches()) {
							String emailId = argument;
							if(userRepository.existsByEmailId(emailId)){
								// Email ID Already taken
								responseText = "Email ID *" + argument + "* " +
										"is already taken, " +
										"please get some other";
								return new TelegramResponse(
										chatId,
										responseText
								);
							}

							User user = new User(chatId,
									emailId,
									emailServerConfig.getTargetAlias());

							String response = null;
							try {

								response = this.createEmail(user);

							}catch (EmailAliasNotCreatedExecption emailAliasNotCreatedExecption){
								log.error("Exception " +
												  emailAliasNotCreatedExecption.getMessage());
								responseText = "Email address is already taken." +
										"\n" +
										"Please try something else.";
								return new TelegramResponse(
										chatId,
										responseText
								);

							}catch (HttpClientErrorException httpClientErrorException){

								log.error("Exception " + httpClientErrorException.getMessage());
								responseText =  "Email address is already taken." +
										"\nPlease try something else.";
								return new TelegramResponse(
										chatId,
										responseText
								);
							}

							if(response!=null) {
								userRepository.save(user);
								responseText =  "Email successfully created" +
										" - " +
										"" +
										"*"+argument+"*" +
										"\n See all your emails @ /emails";

								return new TelegramResponse(
										chatId,
										responseText
								);
							}

							log.error(response);
							responseText = "Something bad just happened with " +
									"me." +
									"Stay back till I get fixed.";
							return new TelegramResponse(
									chatId,
									responseText
							);
						}
						else if(!matcher.matches()){

							if (argument.contains("@") ||
									argument.contains("-")) {
								responseText = "EmailId contains illegal chars.";
								return new TelegramResponse(
										chatId,
										responseText
								);
							}

							responseText = "Pick a domain for the emailId: " +
									"*"+argument+"*";
							List<String> emailsHosts =
									emailServerConfig.getHosts();

							int buttonPerRow = 2;
							int buttonsRow = (int) Math.ceil(
									(double)emailsHosts.size()/buttonPerRow);

							List<List<InlineKeyboardButton>> buttonList =
									new ArrayList<>(buttonsRow);

							for(int i=0;i<buttonsRow;++i)
								buttonList.add(new ArrayList<>());

							InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


							for(int i=0; i < emailsHosts.size(); ++i){
								InlineKeyboardButton btn = new InlineKeyboardButton();
								btn.setText(emailsHosts.get(i));
								btn.setCallback_data(
										"/create "
										 + argument
										 + "@"+
										 emailsHosts.get(i));

								buttonList.get(i / buttonPerRow).add(btn);
							}

							inlineKeyboardMarkup.setInlineKeyboardButtonList(
									buttonList);

							return new TelegramResponse(
									chatId,
									responseText,
									inlineKeyboardMarkup
							);

						}
					}
					else if(argument == null) {
						// Setup interactive with inline keyboard messages.
						responseText = "Please use command like " +
								"/create username";
						return new TelegramResponse(
								chatId,
								responseText
						);
					}

					responseText = "Email id should be of the form: `*@("+
							String.join("|", emailServerConfig.getHosts())+")`";
					return new TelegramResponse(
							chatId,
							responseText
					);
				}

			case "/help":
				responseText = "Create disposable email addresses to " +
						"protect you against spam and newsletters." +
						"E-Mail forwarding made easy.\n" +
						"I am open source and runs on open source services." +
						"You can find my heart, soul and brain at:" +
						" https://github.com/r0hi7/Trashemail\n\n"+
						"Currently, I support:\n" +
						"/create - That can get you one(or more) " +
						"custom disposable emails.\n" +
						"/delete - If you are getting spams for any mail id, " +
						"just delete it because it is disposable.\n" +
						"/emails - Get the list of all the emailIds " +
						"that belongs to you.\n" +
						"/help - this help message.\n";
				return new TelegramResponse(
						chatId,
						responseText
				);

			case "/emails":
				String response = "Currently, you have below mentioned " +
						"emails with you.\n*";
				List<User> allEmailsWithUser = userRepository.findByChatId(
						chatId);

				for(User emailWithUser: allEmailsWithUser){
					response += emailWithUser + "\n";
				}

				response+="*";
				return new TelegramResponse(
						chatId,
						response
				);

			case "/delete":
				if(argument == null){
					responseText = "Pick an email to delete.";

					List<User> emailsWithUser = userRepository.findByChatId(
							chatId);

					int buttonPerRow = 1;
					int buttonsRow = (int) Math.ceil(
							(double)emailsWithUser.size()/buttonPerRow);
					List<List<InlineKeyboardButton>> buttonList =
							new ArrayList<>(buttonsRow);

					for(int i=0;i<buttonsRow;++i)
						buttonList.add(new ArrayList<>());

					InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

					for(int i=0; i < emailsWithUser.size(); ++i){
						InlineKeyboardButton btn = new InlineKeyboardButton();
						btn.setText(emailsWithUser.get(i).toString());
						btn.setCallback_data(
								"/delete " +
								emailsWithUser.get(i).toString());

						buttonList.get(i / buttonPerRow).add(btn);
					}

					inlineKeyboardMarkup.setInlineKeyboardButtonList(
							buttonList);
					return new TelegramResponse(
							chatId,
							responseText,
							inlineKeyboardMarkup
					);
				}
				else {
					String emailRegex = "^[A-Za-z0-9._%+-]+@(" +
							String.join("|", emailServerConfig.getHosts()) +
							")$";

					Pattern pattern = Pattern.compile(emailRegex);
					Matcher matcher = pattern.matcher(argument);

					// A valid domain email is inputted by the user
					if (matcher.matches()) {
						String emailId = argument;
						User user = userRepository.findByEmailId(emailId);
						// user should only delete email owned by user.
						if (userRepository.existsByEmailId(emailId)) {
							if (((Long) chatId).equals(user.getChatId())) {
								responseText = this.deleteEmail(user);
								return new TelegramResponse(
										chatId,
										responseText
								);
							}
						} else {
							responseText = "*Email not registered " +
									"to this user ..*";
							return new TelegramResponse(
									chatId,
									responseText
							);
						}

					} else {
						if (argument.isEmpty()) {
							responseText = "Please use command like " +
									"/delete <custom_name>@" +
									"" + String.join(
											"|",
											emailServerConfig.getHosts());
							return new TelegramResponse(
									chatId,
									responseText
							);
						}
						responseText = "Email id should be of the form: `*@(" +
								String.join(
										"|",
										emailServerConfig.getHosts()) + ")`";
						return new TelegramResponse(
								chatId,
								responseText
						);
					}
				}

				break;

			default:
				responseText = "I dont understand that ...\n " +
						"I only understand few commands.\n" +
						"1. /create <user>\n" +
						"2. /delete\n" +
						"3. /help\n" +
						"4. /emails\n";
				return new TelegramResponse(
						chatId,
						responseText
				);
		}

		return null;
	}

}
