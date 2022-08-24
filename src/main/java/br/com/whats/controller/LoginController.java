package br.com.whats.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenvia.api.sdk.webhook.MessageEvent;

@RestController
@RequestMapping("/receive")
public class LoginController {
	
	@PostMapping
	public String login(@RequestBody MessageEvent message) {
		
		System.out.println(message);
		
		return"";
	}

}
