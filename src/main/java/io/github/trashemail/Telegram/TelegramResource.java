package io.github.trashemail.Telegram;

import io.github.trashemail.Telegram.DTO.TelegramRequest;
import io.github.trashemail.Telegram.DTO.TelegramResponse;
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

	private static final Logger log = LoggerFactory.getLogger(TelegramResource.class);

    @PostMapping(value = "/new-message")
    public TelegramResponse messageHandler(@RequestBody TelegramRequest telegramRequest) {
    	
    	String response = null;
    	
    	try {
    		response = telegramRequestHandler.handleRequest(
    			telegramRequest.getMessage().getChat().getId(),
    			telegramRequest.getMessage().getText()
    		);
    	}
    	catch(HttpClientErrorException httpClientException) {
    		response = httpClientException.getMessage();
    	}catch(Exception e){
    		e.printStackTrace();
    		log.error(e.getMessage());
    		return new TelegramResponse(
    			1,
				"Unprocessed"
			);
		}
    	
    	return new TelegramResponse(
    		telegramRequest.getMessage().getChat().getId(),
    		response);
    }
}