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

			user.setIsActive(false);
			userRepository.save(user);

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
						"I am a *disposable email* id generator bot that " +
						"can:\n" +
						"1. Quickly generate a disposable email for you," +
						" no email management hassle \uD83D\uDE0B\t\n" +
						"2. Quickly delete those email addresses\n" +

						"For now to get started try\n" +
						"* /create <user>\n" +
						"* /delete\n" +
						"* /help\n" +
						"* /list_ids\n";

				return new TelegramResponse(
						chatId,
						responseText
				);

			case "/create":
				if(userRepository.findByChatIdAndIsActiveTrue(chatId).size()
						>= trashemailConfig.getMaxEmailsPerUser()) {
					responseText =  "Only " +
							trashemailConfig.getMaxEmailsPerUser() + " " +
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
							Now offer him interactive response.
							*/
						String emailRegex = "^[A-Za-z0-9._%+-]+@(" +
								String.join("|",
											emailServerConfig.getHosts()) +")$";

						Pattern pattern = Pattern.compile(emailRegex);
						Matcher matcher = pattern.matcher(argument);

						// A valid domain email is inputted by the user
						if (matcher.matches()) {
							String emailId = argument;
							if(userRepository.existsByEmailIdAndIsActiveTrue(
									emailId)){
								// Email ID Already taken
								responseText = "Email ID *" + argument + "* " +
										"is already taken, " +
										"please try some other email id.";
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
								// Check if the same user has taken the same
								// email before, then it would exist in DB so
								// just set isActive to true.
								User existingUser =
										userRepository.findByEmailIdAndChatId(
										emailId,
										chatId);
								if(existingUser == null)
									userRepository.save(user);
								else{
									existingUser.setIsActive(true);
									userRepository.save(existingUser);
								}
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
						"I am open source and runs on open source services.\n" +

						"Currently, I support:\n" +
						"/create - That can get you one(or more) " +
						"custom disposable emails.\n" +
						"/delete - If you are getting spams for any mail id, " +
						"just delete it because it is disposable.\n" +
						"/list_ids - Get the list of all the email Ids "+
						"that belongs to you.\n" +
						"/help - this help message.\n";
				return new TelegramResponse(
						chatId,
						responseText
				);

			case "/list_ids":
				String response = "Currently, you have these email ids with " +
						"you. \n";
				List<User> allEmailsWithUser =
						userRepository.findByChatIdAndIsActiveTrue(
						chatId);

				for(User emailWithUser: allEmailsWithUser){
					response += emailWithUser + "\n";
				}

				return new TelegramResponse(
						chatId,
						response
				);

			case "/delete":
				if(argument == null){
					responseText = "Pick an email to delete.";

					List<User> emailsWithUser =
							userRepository.findByChatIdAndIsActiveTrue(
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
						User user = userRepository.findByEmailIdAndIsActiveTrue(
								emailId);
						// user should only delete email owned by user.
						if (userRepository.existsByEmailIdAndIsActiveTrue(emailId)) {
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

			case "/follow":
				String follow_response = "Follow me on: \n" +
						"Twitter : https://twitter.com/sehgal_rohit" +
						"GitHub : https://github.com/r0hi7";

				return new TelegramResponse(
						chatId,
						follow_response
				);

			case "/sponsor":
				String sponsor_response = "Average running cost of this " +
						"per-month is $30, if you like the idea and would " +
						"like to sponsor then you can buy me a " +
						"coffee: https://www.buymeacoffee.com/r0hi7";
				return new TelegramResponse(
						chatId,
						sponsor_response
				);

			case "/like":
				String like_response = "*If* you like the idea and this " +
						"product, do drop a star at github repo (where my " +
						"hear and soul lives).\n Every star " +
						"really " +
						"motivates me :)\n"+
						"https://github.com/TrashEmail/TrashEmail";
				return new TelegramResponse(
						chatId,
						like_response
				);

			default:
				responseText = "I don't understand that ...\n " +
						"I only understand few commands.\n" +
						"1. /create <user>\n" +
						"2. /delete\n" +
						"3. /help\n" +
						"4. /list_ids\n"+
						"5. /follow\n" +
						"6. /like\n"+
						"7. /sponsor";

				return new TelegramResponse(
						chatId,
						responseText
				);
		}

		return null;
	}

}
