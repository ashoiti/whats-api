package br.com.whats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zenvia.api.sdk.webhook.MessageEvent;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@GetMapping
	public String login() {
//		
		System.out.println("ss");
		
		return"";
	}

}
