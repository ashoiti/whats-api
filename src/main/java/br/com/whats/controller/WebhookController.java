package br.com.whats.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvia.api.sdk.webhook.MessageEvent;

import br.com.whats.dto.MessageEventDto;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
	
	@PostMapping
	public String login(@RequestBody MessageEvent message) {
		
		String messageJson = message.toString();
		
		try {
			MessageEventDto messageDto = new ObjectMapper().readValue(messageJson, MessageEventDto.class);
			String content = getContent(messageDto);
			
			System.out.println(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return"";
	}
	
	private boolean checkGreeting(String text) {
		return text.toLowerCase().startsWith("oi") || text.toLowerCase().startsWith("ola");
	}

	private String getContent(MessageEventDto message) {
		
		if (message.getMessage() != null && message.getMessage().getContents() != null && 
				message.getMessage().getContents().isEmpty()) {
		
			return message.getMessage().getContents().get(0).getText();
		} 
		return null;
	}

}
