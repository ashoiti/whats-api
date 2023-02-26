package br.com.whats.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;

import br.com.whats.model.Project;

@Service
public class PdfService {
	
	public void generatePDF(Project project) {
		
		try {
			URL resource = getClass().getClassLoader().getResource("templates/projectTemplate.pdf");
			
	        PDDocument pDDocument = PDDocument.load(new File(resource.toURI()));
	        PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
	        PDField field = pDAcroForm.getField("name");
	        field.setValue(project.getName());
	        field = pDAcroForm.getField("store");
	        field.setValue(project.getStore());
	        field = pDAcroForm.getField("activation");
	        field.setValue(project.getActivation());
	        field = pDAcroForm.getField("alignment");
	        field.setValue(project.getAlignment());
	        field = pDAcroForm.getField("owner");
	        field.setValue(project.getResponsible());
	        field = pDAcroForm.getField("helper");
	        field.setValue(project.getAssistant());
	        field = pDAcroForm.getField("description");
	        field.setValue(project.getDescription());
	        
			if (project.getImage() != null) {
		        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pDDocument, project.getImage().getContent(), "a.png");
		        
		        PDPage my_page = new PDPage();
		        pDDocument.addPage(my_page);
		        PDPageContentStream contents = new PDPageContentStream(pDDocument, pDDocument.getPage(1));
		        contents.drawImage(pdImage, 25, 150, 500, 500);  
		        contents.setHorizontalScaling(50);
		        contents.close();  
			}
	        
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        pDDocument.save(out);
	        
	        String storageLocation = buildStorageLocation(project.getName());
	        
			pDDocument.save(storageLocation);
	        pDDocument.close();
	        
	        project.setStorage(storageLocation);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private String buildStorageLocation(String name) {
		return "C:\\Users\\Andre\\Downloads\\" + name + ".pdf";
	}
}