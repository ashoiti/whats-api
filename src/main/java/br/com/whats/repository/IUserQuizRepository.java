package br.com.whats.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.User;
import br.com.whats.model.UserQuiz;
import br.com.whats.model.UserQuizKey;

public interface IUserQuizRepository extends JpaRepository<UserQuiz, UserQuizKey> {
	
	List<UserQuiz> findByUser(User user);

}
