package io.github.teledot.Telegram;

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

    @PostMapping(value = "/new-message")
    public TelegramResponse messageHandler(@RequestBody TelegramRequest telegramRequest) {
    	
    	String response;
    	
    	try {
    		response = telegramRequestHandler.handleRequest(
    			telegramRequest.getMessage().getChat().getId(),
    			telegramRequest.getMessage().getText()
    		);
    	}
    	catch(HttpClientErrorException httpClientException) {
    		response = httpClientException.getMessage();
    	}
    	
    	return new TelegramResponse(
    		telegramRequest.getMessage().getChat().getId(),
    		response);
    }
}