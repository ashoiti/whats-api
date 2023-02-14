package br.com.whats.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import br.com.whats.dto.ImageDto;
import br.com.whats.model.Image;
import br.com.whats.repository.IImageRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {
	
	@Value("${zenvia.token}")
	private String token;
	
	@Value("${zenvia.fileUrl}")
	private String url;
	
	@Value("${zenvia.from}")
	private String from;
	
	@Autowired
	private IImageRepository repository;
	
	public Image saveImage(String sourceUrl, String mimeType) {
		
		ImageDto imageDto = saveImageZenviaRep(sourceUrl, mimeType);
		
		byte[] imageContent = getImageZenviaRep(imageDto.getId());
		
		Image img = new Image();
		img.setMimetype(mimeType);
		img.setName(imageDto.getName());
		img.setContent(imageContent);
		img.setZenviaUrl(imageDto.getId());
		Image image = repository.save(img);
		
		return image;
		
	}
	
	public ImageDto saveImageZenviaRep(String sourceUrl, String mimeType) {
		try {
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
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	public byte[] getImageZenviaRep(String urlSource) {
		try {
			HttpResponse<InputStream> asBinary = Unirest.get(url + "/" + urlSource)
			  .asBinary();
			System.out.println(asBinary);
			
		    return IOUtils.toByteArray(asBinary.getBody());
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
		
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-API-TOKEN", token);
		
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
