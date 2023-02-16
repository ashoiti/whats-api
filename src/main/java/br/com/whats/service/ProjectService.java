package br.com.whats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Project;
import br.com.whats.repository.IProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private IProjectRepository repository;
	
	@Autowired
	private PdfService pdfService;
	
	public void save(Project project) {
		
		repository.save(project);
		
	}
	
	public void generatePdfProject(Project project) {
		
		pdfService.generatePDF(project);
		
	}
	
}