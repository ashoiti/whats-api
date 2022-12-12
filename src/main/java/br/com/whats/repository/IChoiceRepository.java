package br.com.whats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.Choice;
import br.com.whats.model.Question;

public interface IChoiceRepository extends JpaRepository<Choice, Integer> {
	
	List<Choice> findByName(String name);
	
	List<Choice> findByNameAndQuestion(String name, Question question);

}
