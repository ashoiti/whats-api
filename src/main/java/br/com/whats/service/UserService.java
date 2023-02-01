package br.com.whats.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Choice;
import br.com.whats.model.Question;
import br.com.whats.model.Quiz;
import br.com.whats.model.User;
import br.com.whats.model.UserQuestion;
import br.com.whats.model.UserQuestionKey;
import br.com.whats.model.UserQuiz;
import br.com.whats.model.UserQuizKey;
import br.com.whats.repository.IUserQuestionRepository;
import br.com.whats.repository.IUserQuizRepository;
import br.com.whats.repository.IUserRepository;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository repository;
	
	@Autowired
	private IUserQuizRepository userQuizRepository;
	
	@Autowired
	private IUserQuestionRepository userQuestionRepository;
	
	public User findByRegistry(String registry) {
		
		return repository.findByRegistry(registry);
		
	}
	
	public Quiz findQuizByUser(User user) {
		
		List<UserQuiz> quizByUser = userQuizRepository.findByUser(user);
		
		if (quizByUser == null || quizByUser.isEmpty()) {
			return null;
		}
		
		List<Quiz> quizzes = new ArrayList<Quiz>();
		
		for (UserQuiz userQuiz : quizByUser) {
			if (!userQuiz.getAnswered().equals(userQuiz.getQuiz().getQuestions().size())) {
				quizzes.add(userQuiz.getQuiz());
			}
		}
		
		if (quizzes.isEmpty()) {
			return null;
		}
		return quizzes.get(0);
		
	}
	
	public Quiz findQuizAnsweredByUser(User user) {
		
		List<UserQuiz> quizByUser = userQuizRepository.findByUser(user);
		
		if (quizByUser == null || quizByUser.isEmpty()) {
			return null;
		}
		
		for (UserQuiz userQuiz : quizByUser) {
			if (userQuiz.getAnswered().equals(userQuiz.getQuiz().getQuestions().size())) {
				return userQuiz.getQuiz();
			}
		}
		
		return null;
		
	}
	
	public List<Question> findQuestionAnsweredsByUser (User user) {
	
		List<UserQuestion> questionByUser = userQuestionRepository.findByUser(user);
		
		if (questionByUser == null || questionByUser.isEmpty()) {
			return null;
		}
		
		List<Question> questions = new ArrayList<Question>();
		
		for (UserQuestion userQuestion : questionByUser) {
			questions.add(userQuestion.getQuestion());
		}
		
		return questions;
	}
	
	public boolean isQuestionAnswered(User user, Question question) {
		UserQuestionKey id = new UserQuestionKey();
		id.setQuestionId(question.getId());
		id.setUserId(user.getId());
		
		return userQuestionRepository.findById(id).isPresent();
	}
	
	public void answerQuestion(User user, Question question, Choice answer) {
		
		UserQuestionKey id = new UserQuestionKey();
		id.setQuestionId(question.getId());
		id.setUserId(user.getId());
		
		UserQuestion userQuestion = new UserQuestion();
		userQuestion.setId(id);
		userQuestion.setDate(LocalDateTime.now());
		userQuestion.setAnswer(answer);
		userQuestion.setQuestion(question);
		userQuestion.setUser(user);
		
		userQuestionRepository.save(userQuestion);
	}
	
	public UserQuiz getQuizUser(User user, Quiz quiz) {
		UserQuizKey id = new UserQuizKey();
		id.setQuizId(quiz.getId());
		id.setUserId(user.getId());
		
		return userQuizRepository.findById(id).orElseThrow(() -> new RuntimeException("UserQuiz not found!!"));
		
	}
	
	public void updateQuizUser(UserQuiz quizUser) {
		userQuizRepository.save(quizUser);
	}
	 
}