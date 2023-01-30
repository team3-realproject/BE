package com.example.alba_pocket.test;

import com.example.alba_pocket.dto.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
@RequiredArgsConstructor
public class GreetingService {
    public Greeting greeting(HelloMessage message) {
        String name = "jaeha";


        return new Greeting(name + ":" + HtmlUtils.htmlEscape(message.getMessage()));
    }
}
