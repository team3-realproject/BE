package com.example.alba_pocket.test;

import com.example.alba_pocket.dto.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return greetingService.greeting(message);
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getMessage()) + "!");
    }

}