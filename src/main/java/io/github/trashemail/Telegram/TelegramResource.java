package io.github.trashemail.Telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.trashemail.Telegram.DTO.TelegramResponse;
import io.github.trashemail.Telegram.DTO.messageEntities.CallbackQuery;
import io.github.trashemail.Telegram.DTO.messageEntities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/telegram/")
public class TelegramResource {
	
	@Autowired
	TelegramRequestHandler telegramRequestHandler;
	@Autowired
	SendTelegramMessage sendTelegramMessage;

	private static final Logger log = LoggerFactory.getLogger(
			TelegramResource.class);

    @PostMapping(value = "/new-message")
    public TelegramResponse messageHandler(
    		@RequestBody JsonNode telegramMessageRequest) {
    	
    	Object response = null;
    	String responseText = null;
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			JsonNode jsonCallbackQueryNode =
					telegramMessageRequest.get("callback_query");

			// This request is callback message or edited message.
			if(jsonCallbackQueryNode != null){
				CallbackQuery callbackQuery =
						objectMapper.convertValue(jsonCallbackQueryNode,
												  CallbackQuery.class);
				// Process callback Query data.
				response = telegramRequestHandler.handleRequest(
						callbackQuery.getFrom().getId(),
						callbackQuery.getData()
				);
			}

			// This is Message.
			else{
				JsonNode jsonMessageNode = telegramMessageRequest.get("message");
				if(jsonMessageNode != null) {
					// this is surely a message/
					Message message = objectMapper.convertValue(
							jsonMessageNode, Message.class);
					// Process message and respond.
					response = telegramRequestHandler.handleRequest(
							message.getChat().getId(),
							message.getText()
					);
				}
			}


    	}
    	catch(HttpClientErrorException httpClientException) {
    		responseText = httpClientException.getMessage();
			return new TelegramResponse(
					1,
					responseText
			);
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error(e.getMessage());
    		return new TelegramResponse(
    			1,
				responseText
			);
		}
    	
    	return (TelegramResponse) response;
    }
}