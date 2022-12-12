package br.com.whats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Choice;
import br.com.whats.model.Question;
import br.com.whats.repository.IChoiceRepository;

@Service
public class ChoiceService {
	
	@Autowired
	private IChoiceRepository repository;
	
	public boolean isChoice(String choice) {
		
		return !repository.findByName(choice).isEmpty();
		
	}
	
	public Choice findByNameQuestion(String name, Question question) {
		
		return repository.findByNameAndQuestion(name, question).get(0);
	}
	
	public Choice findById(Integer id) {
		
		return repository.findById(id).get();
	}
	
}