package br.com.whats.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
	
	@Value("${zenvia.token}")
	private String token;
	
	@Value("${zenvia.url}")
	private String url;
	
	@Value("${zenvia.from}")
	private String from;
	
	public void sendMessage(String to, String text) {
		try {
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post(url)
			  .headers(getHeaders())
			  .body(new ObjectMapper().writeValueAsString(getBodyText(to, text)))
			  .asJson();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void sendListMessage(String to, String title, List<String> choices) {
		try {
			
			String json = new ObjectMapper().writeValueAsString(getListText(to, title, choices));
			System.out.println(json);
			
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post(url)
			  .headers(getHeaders())
			  .body(json)
			  .asJson();
			
			System.out.println(jsonResponse.getStatus() + " - " + jsonResponse.getStatusText());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void sendButtonMessage(String to, String title, List<String> choices, boolean showFooter) {
		try {
			
			String json = new ObjectMapper().writeValueAsString(getButtonBodyText(to, title, choices, showFooter));
			System.out.println(json);
			
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post(url)
			  .headers(getHeaders())
			  .body(json)
			  .asJson();
			
			System.out.println(jsonResponse.getStatus() + " - " + jsonResponse.getStatusText());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-API-TOKEN", token);
		
		return headers;
	}
	
	private Map<String, Object> getBody(String to) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("from", from);
		body.put("to", to);
		
		return body;
	}

	private Map<String, Object> getBodyText(String to, String text) {
		Map<String, Object> body = getBody(to);
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "text");
		content.put("text", text);
		
		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		
		return body;
	}
	
	private Map<String, Object> getListText(String to, String title, List<String> choices) {
		Map<String, Object> body = getBody(to);
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "list");
		content.put("body", title);
		content.put("button", "Responder");
		
		Map<String, Object> section = new HashMap<String, Object>();
		section.put("title", title.length() > 23 ? title.substring(0, 23) : title);
		
		ArrayList<Map<String, Object>> rows = new ArrayList<>();
		for (String choice : choices) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("title", choice.length() > 23 ? choice.substring(0, 23) : choice);
			row.put("id", choice);
//			row.put("description", choice.length() > 71 ? choice.substring(0, 71) : choice);
			rows.add(row);
		}
		section.put("rows", rows);
		
		ArrayList<Map<String, Object>> sections = new ArrayList<>();
		sections.add(section);
		content.put("sections", sections);
		
		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		
		return body;
	}
	
	private Map<String, Object> getButtonBodyText(String to, String title, List<String> choices, boolean showFooter) {
		Map<String, Object> body = getBody(to);
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "button");
		content.put("body", title);
		if (showFooter) {
			content.put("footer", "Escolha uma opção abaixo:");
		}
		
		ArrayList<Map<String, Object>> buttons = new ArrayList<>();
		for (String choice : choices) {
			Map<String, Object> button = new HashMap<String, Object>();
			button.put("title", choice.length() > 20 ? choice.substring(0, 20) : choice);
			button.put("id", choice);
			buttons.add(button);
		}
	
		content.put("buttons", buttons);

		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		return body;
	}


}
