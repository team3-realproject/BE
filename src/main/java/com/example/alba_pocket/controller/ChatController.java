//package com.example.alba_pocket.controller;
//
//import com.example.alba_pocket.dto.Greeting;
//import com.example.alba_pocket.dto.MessageDto;
//import com.example.alba_pocket.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//
//@Configuration
//@RequiredArgsConstructor
//public class ChatController {
//
//    private final ChatService chatService;
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(MessageDto message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return chatService.greeting(message);
////        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getMessage()) + "!");
//    }
//}
