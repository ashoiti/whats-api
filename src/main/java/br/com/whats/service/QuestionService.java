package br.com.whats.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.whats.model.Question;
import br.com.whats.model.Quiz;
import br.com.whats.model.User;
import br.com.whats.repository.IQuestionRepository;

@Service
public class QuestionService {
	
	@Autowired
	private IQuestionRepository repository;
	
	@Autowired
	private UserService userService;
	
	public Question findById(Integer id) {
		
		return repository.findById(id).get();
		
	}
	
	public Question getNextQuestion(User user) {
		//verifica se usuario tem questionario
		Quiz quiz = userService.findQuizByUser(user);
		
		//busca as questões respondidas
		List<Question> questionsAnswereds = userService.findQuestionAnsweredsByUser(user);
		
		//filtra as questões respondidas com o total e gera a questão
		Question question = filterQuestionsNotAnswered(quiz.getQuestions(), questionsAnswereds);
		return question;
	}
	
	public Question filterQuestionsNotAnswered(List<Question> questionsTotal, List<Question> questionsAnswereds) {
		
		if (questionsAnswereds == null || questionsAnswereds.isEmpty()) {
			return questionsTotal.stream().findFirst().get();
		}
		
		List<Question> notAnswered = new ArrayList<Question>();
		for (Question question : questionsTotal) {
			boolean found = true;
			for (Question questionAnswered : questionsAnswereds) {
				if (questionAnswered.getId().equals(question.getId())) {
					found = false;
					break;
				}
			}
			if (found) {
				notAnswered.add(question);
			}
		}
		
		if (notAnswered == null || notAnswered.isEmpty()) {
			return null;
		}
		return notAnswered.stream().findFirst().get();
		
	}
	
}