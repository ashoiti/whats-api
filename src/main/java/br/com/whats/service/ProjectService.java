package br.com.whats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Project;
import br.com.whats.repository.IProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private IProjectRepository repository;
	
	public void save(Project project) {
		
		repository.save(project);
		
	}
	
}