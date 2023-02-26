package br.com.whats.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import br.com.whats.dto.ImageDto;
import br.com.whats.model.Image;
import br.com.whats.repository.IImageRepository;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private IImageRepository rep;
	
	private String url = "https://api.zenvia.com/v2/files";
	private String token = "lywCOVGQl0tgUNfrddbnpkSmaBF-TJI-rF-q";
	
	@GetMapping(value = "/generate-zenvia-image")
	public ImageDto saveImageZenviaRep(String sourceUrl, String mimeType) {
		try {
			
			sourceUrl = "https://chat.zenvia.com/storage/files/d0708be46fc20f0444f1f418f2d72cc60c7ed048bbba6cb9286dc28feaedecb4.bin";
			mimeType = "image/jpeg";
			
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post(url)
			  .headers(getHeaders())
			  .body(new ObjectMapper().writeValueAsString(getBody(sourceUrl, mimeType)))
			  .asJson();
			
			String id = (String) jsonResponse.getBody().getObject().get("id");
			String name = (String) jsonResponse.getBody().getObject().get("name");
			
			ImageDto dto = new ImageDto();
			dto.setId(id);
			dto.setName(name);
			
			return dto;
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@GetMapping(value = "/generate-zenvia-image-content")
	public void getImageZenviaRep(@RequestParam String urlSource) {
		try {
			HttpResponse<InputStream> asBinary = Unirest.get(url + "/" + urlSource)
			  .asBinary();
			System.out.println(asBinary);
			
			Image img = new Image();
			img.setContent(IOUtils.toByteArray(asBinary.getBody()));
			rep.save(img);
			
//		    return IOUtils.toByteArray(asBinary.getBody());
			
		} catch (Exception e) {
			System.out.println(e);
//			return null;
		}
	}
		
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Content-Type", "application/json");
		headers.put("X-API-TOKEN", token );
		
		return headers;
	}
	
	private Map<String, Object> getBody(String sourceUrl, String mimeType) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("sourceUrl", sourceUrl);
		body.put("mimeType", mimeType);
		body.put("autoDeleteMinutes", 5);
		
		Map<String, Object> sourceHeader = new HashMap<String, Object>();
		sourceHeader.put("Authorization", "Basic" + " " + token);
		sourceHeader.put("X-Custom-Token", "TOKEN");
		body.put("sourceHeaders", sourceHeader);
		
		return body;
	}

}
