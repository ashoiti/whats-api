package br.com.whats.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
		body.put("from", "aerial-smartphone");
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
	      .getResourceAsStream("/pdf-java-output.pdf");
	    byte[] byteArray = IOUtils.toByteArray(in);
	    
	    Map<String, String> headers = new HashMap<String, String>();
//		headers.put("content-type", "multipart/form-data;  boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
	    headers.put("content-type", "application/pdf");
		headers.put("content-disposition", "inline;filename=my-image.pdf");
		headers.put("cache-control", "no-cache");
		headers.put("X-API-TOKEN", "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
		
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
		headers.put("X-API-TOKEN", "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
		
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
	        
			
	        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pDDocument, rep.getById((long)1).getContent(), "a.png");
	        
	        PDPage my_page = new PDPage();
	        pDDocument.addPage(my_page);
	        PDPageContentStream contents = new PDPageContentStream(pDDocument, pDDocument.getPage(1));
	        contents.drawImage(pdImage, 25, 150, 500, 500);  
	        contents.setHorizontalScaling(50);
	        contents.close();  
	        
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        pDDocument.save(out);
	        
	        pDDocument.save("C:\\Users\\Andre\\Downloads\\pdf-java-output.pdf");
	        pDDocument.close();
	        
	        sendEmail();
	        
//	        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
//	        byte[] byteArray = IOUtils.toByteArray(inputStream);
//	        
////	        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
////	        byte[] readAllBytes = in.readAllBytes();
//	        
//	        Map<String, String> headers = new HashMap<String, String>();
//			headers.put("X-API-TOKEN", "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
//			headers.put("content-type", "application/pdf");
//			headers.put("content-disposition", "inline;filename=my-image.pdf");
//			headers.put("cache-control", "no-cache");
//			
//	        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.zenvia.com/v2/files")
//					.headers(headers )
//					.body(byteArray)
//
//					.asJson();
//
//		    String url = (String) jsonResponse.getBody().getObject().get("url");
//		    
//		    headers = new HashMap<String, String>();
//			headers.put("Content-Type", "application/json");
//			headers.put("X-API-TOKEN", "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
//			
//			Map<String, Object> body = getFileBody(url);
//			jsonResponse 
//			  = Unirest.post("https://api.zenvia.com/v2/channels/whatsapp/messages")
//			  .headers(headers )
//			  .body(new ObjectMapper().writeValueAsString(body))
//			  .asJson();
	        
	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	private void sendEmail() {
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("andre.shoiti@gmail.com", "tbmvyszhyoadcoml");
		    }
		});
		
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("andre.shoiti@gmail.com"));
			message.setRecipients(
			  Message.RecipientType.TO, InternetAddress.parse("andre.shoiti@gmail.com"));
			message.setSubject("Mail Subject");
			
			String msg = "This is my first email using JavaMailer";
			
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
			
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			attachmentBodyPart.attachFile(new File("C:\\Users\\Andre\\Downloads\\pdf-java-output.pdf"));
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			multipart.addBodyPart(attachmentBodyPart);
			
			message.setContent(multipart);
			
			Transport.send(message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "/generate-zenvia-image")
	public void generateZenviaImage() {
		try {
			HttpResponse<InputStream> asBinary = Unirest.get("https://chat.zenvia.com/storage/files/5aab255203c10d7557b1de10d5185bbd2b0b4a879634ae81ccc0beb58dd73fdb.bin")
			  .asBinary();
			System.out.println(asBinary);
			
			
		    byte[] byteArray = IOUtils.toByteArray(asBinary.getBody());
			Image img = new Image();
			img.setContent(byteArray);
			rep.save(img);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
	}
	
	@GetMapping(value = "/save-zenvia-image")
	public void saveZenviaImage() {
		try {
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-API-TOKEN", "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
			
			HttpResponse<JsonNode> jsonResponse 
			  = Unirest.post("https://api.zenvia.com/v2/files")
			  .headers(headers)
			  .body(new ObjectMapper().writeValueAsString(getBody("https://chat.zenvia.com/storage/files/2b3e5d68ea3f3b362cb5ca27e17c187ef3cf7404847266d9c250bc85fdb43a79.bin", "image/jpeg")))
			  .asJson();
			
			jsonResponse.getBody().getObject().get("id");
			
			
			
		} catch (Exception e) {
			
			
		}
			    
	}
	
	private Map<String, Object> getBody(String sourceUrl, String mimeType) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("sourceUrl", sourceUrl);
		body.put("mimeType", mimeType);
		body.put("autoDeleteMinutes", 5);
		
		Map<String, Object> sourceHeader = new HashMap<String, Object>();
		sourceHeader.put("Authorization", "Basic" + " " + "xDFcVc4hcUJaVVAkUuo-9mG94m9lmLGDO84U");
		sourceHeader.put("X-Custom-Token", "TOKEN");
		body.put("sourceHeaders", sourceHeader);
		
		return body;
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
