package br.com.whats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.whats.model.Question;

public interface IQuestionRepository extends JpaRepository<Question, Integer> {
	
	@Query("SELECT SUM(q.points) FROM Question q WHERE q.quiz.id = :id_quiz")
    Integer selectPoints(@Param("id_quiz") Integer id_quiz);
	

}
