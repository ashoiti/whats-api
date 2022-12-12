package br.com.whats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.User;
import br.com.whats.model.UserQuestion;
import br.com.whats.model.UserQuestionKey;

public interface IUserQuestionRepository extends JpaRepository<UserQuestion, UserQuestionKey> {
	
	List<UserQuestion> findByUser(User user);

}
