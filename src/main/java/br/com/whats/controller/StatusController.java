package br.com.whats.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenvia.api.sdk.webhook.MessageStatusEvent;

@RestController
@RequestMapping("/status")
public class StatusController {
	
	@PostMapping
	public String login(@RequestBody MessageStatusEvent message) {
		
		System.out.println(message);
		
		return"";
	}

}
