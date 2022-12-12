package br.com.whats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.Question;

public interface IQuestionRepository extends JpaRepository<Question, Integer> {
	

}
