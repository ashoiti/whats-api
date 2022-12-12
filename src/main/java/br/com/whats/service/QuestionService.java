package br.com.whats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Question;
import br.com.whats.repository.IQuestionRepository;

@Service
public class QuestionService {
	
	@Autowired
	private IQuestionRepository repository;
	
	public Question findById(Integer id) {
		
		return repository.findById(id).get();
		
	}
	
}