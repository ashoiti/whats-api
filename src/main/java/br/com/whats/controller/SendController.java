package br.com.whats.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zenvia.api.sdk.client.Channel;
import com.zenvia.api.sdk.client.apache.Client;
import com.zenvia.api.sdk.contents.Content;
import com.zenvia.api.sdk.contents.TextContent;
import com.zenvia.api.sdk.messages.Message;
import com.zenvia.api.sdk.webhook.MessageEvent;

@RestController
@RequestMapping("/send")
public class SendController {
	
	@PostMapping
	public String send() {
		
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("X-API-TOKEN", "A-6RjIQqfvJRY7JQoKA8ExLQiwYNnf-k06o0");
			
			Map<String, Object> body = getListBody();
			
			System.out.println(new ObjectMapper().writeValueAsString(body));
			
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post("https://api.zenvia.com/v2/channels/whatsapp/messages")
			  .headers(headers )
			  .body(new ObjectMapper().writeValueAsString(body))
			  .asJson();
			
			System.out.println(jsonResponse);
			
			// Initialization with your API token (x-api-token)
//			Client client = new Client("Z3yzT_KXTDTqF5YeGxOVxY7Lq2fiBLchMdhw");
//
//			// Choosing the channel
//			Channel whatsapp = client.getChannel("whatsapp");
//
//			// Creating a text content
//			Content content = new TextContent("some text message here");	
//			
//			Message response = whatsapp.sendMessage("lunar-calliandra", "5511971803191", content);
//			
//			System.out.println(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return"";
	}

	private Map<String, Object> getButtonBody() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("from", "tidy-veil");
		body.put("to", "5511971803191");
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "button");
		content.put("body", "Body text.");
		content.put("footer", "Footer text.");
		
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("type", "text");
		header.put("text", "Header text.");
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("id", "Button ID");
		button.put("title", "Button text");
		
		Map<String, Object> button2 = new HashMap<String, Object>();
		button2.put("id", "Button ID2");
		button2.put("title", "Button text2");

		ArrayList<Map<String, Object>> buttons = new ArrayList<>();
		buttons.add(button);
		buttons.add(button2);
		content.put("buttons", buttons);

		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		return body;
	}
	
	private Map<String, Object> getListBody() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("from", "tidy-veil");
		body.put("to", "5511971803191");
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "list");
		content.put("body", "Body text.");
		content.put("header", "Header text.");
		content.put("footer", "Footer text.");
		content.put("button", "string");
		
		Map<String, Object> section = new HashMap<String, Object>();
		section.put("title", "Section name");
		
		ArrayList<Map<String, Object>> rows = new ArrayList<>();
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("id", "Row id");
		row.put("title", "Row title");
		row.put("description", "Row description");
		
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("id", "Row id2");
		row2.put("title", "Row title2");
		row2.put("description", "Row description2");
		
		Map<String, Object> row3 = new HashMap<String, Object>();
		row3.put("id", "Row id3");
		row3.put("title", "Row title3");
		row3.put("description", "Row description3");
		
		rows.add(row);
		rows.add(row2);
		rows.add(row3);
		
		section.put("rows", rows);

		ArrayList<Map<String, Object>> sections = new ArrayList<>();
		sections.add(section);

		content.put("sections", sections);

		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		return body;
	}

}
