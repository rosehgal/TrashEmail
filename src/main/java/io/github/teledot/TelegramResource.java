package io.github.teledot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram/")
public class TelegramResource {

    @PostMapping(value = "/new-message")
    public TelegramResponse messageHandler(@RequestBody TelegramRequest telegramRequest){
        // Right now I am just sending a Fixed response for any request.
        // You can have your own handlers for each request.
        return new TelegramResponse(
                telegramRequest.getMessage().getChat().getId(),
                "I got your response");
    }
}
