package br.com.whats.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.com.whats.model.Image;
import br.com.whats.repository.IImageRepository;

@RestController
@RequestMapping("/project")
public class UserController {
	
	@Autowired
	private IImageRepository rep;
	
	@GetMapping(
	  value = "/get-file",
	  produces = MediaType.APPLICATION_PDF_VALUE
	)
	public @ResponseBody byte[] getFile() throws IOException {
	    InputStream in = getClass()
	      .getResourceAsStream("/multastransito.pdf");
	    return IOUtils.toByteArray(in);
	}
	
	@GetMapping(value = "/send-file")
	public String downloadFile() throws Exception {
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-API-TOKEN", "JCaHxh_GLNP6by3Yy55nPbzKe7MVWUsWwR8c");
		
		Map<String, Object> body = getFileBody("\"https://zenvia.chat/storage/files/59c2e5f08962ef068217d8554a0cf3bfa5620e772d17f875257f75f1a5b0fbcc.bin\"");
		
		System.out.println(new ObjectMapper().writeValueAsString(body));
		
		HttpResponse<JsonNode> jsonResponse 
		  = Unirest.post("https://api.zenvia.com/v2/channels/whatsapp/messages")
		  .headers(headers )
		  .body(new ObjectMapper().writeValueAsString(body))
		  .asJson();
		
		return "";
	}
	
	private Map<String, Object> getFileBody(String url) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("from", "cherry-cormorant");
		body.put("to", "5511971803191");
		
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("type", "file");
		content.put("fileUrl", url);
		content.put("mimeType", "application/pdf");
		content.put("fileName", "my-image.pdf");
//		content.put("mimeType", "application/pdf");
		
		
		ArrayList<Map<String, Object>> contents = new ArrayList<>();
		contents.add(content);
		
		body.put("contents", contents);
		return body;
	}
	
	@GetMapping
	public String testFile() throws IOException, UnirestException {
	    InputStream in = getClass()
	      .getResourceAsStream("/detran.pdf");
	    byte[] byteArray = IOUtils.toByteArray(in);
	    
	    Map<String, String> headers = new HashMap<String, String>();
//		headers.put("content-type", "multipart/form-data;  boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
	    headers.put("content-type", "application/pdf");
		headers.put("content-disposition", "inline;filename=my-image.pdf");
		headers.put("cache-control", "no-cache");
		headers.put("X-API-TOKEN", "JCaHxh_GLNP6by3Yy55nPbzKe7MVWUsWwR8c");
		
//		File outputFile = new File("a.pdf");
//		FileUtils.writeByteArrayToFile(outputFile, byteArray);
//		
//		TestFile testFile = new TestFile();
//		testFile.setMimeType("application/pdf");
//		testFile.setName("a.pdf");
//		System.out.println(Base64.getEncoder().encodeToString(byteArray));
		HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.zenvia.com/v2/files")
				.headers(headers )
//	            .field("file", testFile)
//				.field("file", in, ContentType.APPLICATION_OCTET_STREAM, "article.txt").
				.body(byteArray)
//				.field("content", Base64.getEncoder().encodeToString(byteArray))
				.asJson();
//		HttpResponse<JsonNode> file = body.asJson();
	    String url = (String) jsonResponse.getBody().getObject().get("url");
		
	    headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("X-API-TOKEN", "JCaHxh_GLNP6by3Yy55nPbzKe7MVWUsWwR8c");
		
		Map<String, Object> body = getFileBody(url);
		jsonResponse 
		  = Unirest.post("https://api.zenvia.com/v2/channels/whatsapp/messages")
		  .headers(headers )
		  .body(new ObjectMapper().writeValueAsString(body))
		  .asJson();
	    
	    return "";
	}
	
	@GetMapping(value = "/generate-image")
	public void generateImage() {
		InputStream in = getClass()
			      .getResourceAsStream("/img2.jpg");
	    byte[] byteArray;
		try {
			byteArray = IOUtils.toByteArray(in);
			Image img = new Image();
			img.setContent(byteArray);
			rep.save(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
	}
	
	@GetMapping(value = "/generate-pdf")
	public void generatePDF() {
		
		try {
			URL resource = getClass().getClassLoader().getResource("templates/projectTemplate.pdf");
			
	        PDDocument pDDocument = PDDocument.load(new File(resource.toURI()));
	        PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
	        PDField field = pDAcroForm.getField("name");
	        field.setValue("This is a first field printed by Java");
	        field = pDAcroForm.getField("store");
	        field.setValue("This is a second field printed by Java");
	        field = pDAcroForm.getField("activation");
	        field.setValue("This is a third field printed by Java");
	        field = pDAcroForm.getField("alignment");
	        field.setValue("This is a fourth field printed by Java");
	        field = pDAcroForm.getField("owner");
	        field.setValue("This is a fifth field printed by Java");
	        field = pDAcroForm.getField("helper");
	        field.setValue("This is a sixth field printed by Java");
	        field = pDAcroForm.getField("description");
	        field.setValue("This is a seventh field printed by Java");
	        
			
	        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pDDocument, rep.getById(1).getContent(), "img.png");
	        
	        PDPage my_page = new PDPage();
	        pDDocument.addPage(my_page);
	        PDPageContentStream contents = new PDPageContentStream(pDDocument, pDDocument.getPage(1));
	        contents.drawImage(pdImage, 25, 150, 500, 500);  
	        contents.setHorizontalScaling(50);
	        contents.close();  
	        
	        pDDocument.save("C:\\Users\\andre.shoiti.yamachi\\DEV\\pdf-java-output.pdf");
	        pDDocument.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}

}

class TestFile {
	private String mimeType;
	private String name;
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
