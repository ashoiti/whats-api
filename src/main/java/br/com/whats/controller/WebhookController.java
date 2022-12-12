package br.com.whats.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvia.api.sdk.webhook.MessageEvent;

import br.com.whats.dto.MessageEventDto;
import br.com.whats.model.Choice;
import br.com.whats.model.Question;
import br.com.whats.model.Quiz;
import br.com.whats.model.User;
import br.com.whats.service.ChoiceService;
import br.com.whats.service.MessageService;
import br.com.whats.service.QuestionService;
import br.com.whats.service.UserService;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
	
	private static final String NOT_UNDERSTAND_MESSAGE = "Não entendi o que quis dizer...Diga um oi para continuar";
	private static final String GREETING_MESSAGE = "Olá! Digite a sua matrícula:";
	private static final String USER_NOT_FOUND = "Matrícula não encontrada...Tente novamente";
	private static final String CORRECT_ANSWER = "Parabéns! Você acertou!!!";
	private static final String WRONG_ANSWER = "Não foi dessa vez...A resposta correta é: ";
	private static final String QUESTION_ANSWERED = "Você já respondeu essa pergunta";
	private static final String NO_QUESTION_TO_ANSWER = "Você já respondeu todas as perguntas disponíveis";
	
	private static Map<String, Map<String, Object>> mapUser = new HashMap<String, Map<String,Object>>();
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChoiceService choiceService;
	
	@Autowired
	private QuestionService questionService;
	
	@PostMapping
	public String login(@RequestBody MessageEvent messageEvent) {
		
		String messageJson = messageEvent.toString();
		
		try {
			MessageEventDto messageDto = new ObjectMapper().readValue(messageJson, MessageEventDto.class);
			String content = getContent(messageDto);
			
			String message = "";
			if (checkGreeting(content)) {
				
				message = GREETING_MESSAGE;
				
			} else if (content.matches("[1-9]")) {
				
				Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
				User user = (User) mapUserData.get("user");
				message = getMessageByMenu(messageDto, content, user);
				
			} else if (isRegistry(content)) {
				
				User findByRegistry = userService.findByRegistry(content.toLowerCase());
				if (findByRegistry != null) {
					message = getRegistryMessage();
					
					//criar sessao usuario
					Map <String, Object> mapUserData = new HashMap<String, Object>();
					mapUserData.put("user", findByRegistry);
					mapUser.put(getFrom(messageDto), mapUserData);
					
				} else {
					message = USER_NOT_FOUND;
				}
				
			} else if (isAnswer(content)) {
				
				//obtem a questão respondida na sessão
				Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
				int idQuestion = (int) mapUserData.get("lastQuestion");
				Question question = questionService.findById(idQuestion);
				
				//verifica se já respondeu 
				User user = (User)mapUserData.get("user");
				if (!userService.isQuestionAnswered(user, question)) {
					
					//verifica se é a resposta correta
					Choice choice = choiceService.findByNameQuestion(content, question);
					boolean isCorrect = question.getAnswer().equals(choice.getId());
					
					//atualiza banco
					userService.answerQuestion(user, question, choice);
					
					//envia mensagem com o feedback
					if (isCorrect) {
						message = CORRECT_ANSWER;
					} else {
						Choice correctAnswer = choiceService.findById(question.getAnswer());
						message = WRONG_ANSWER + correctAnswer.getName();
					}
					messageService.sendMessage(messageDto.getMessage().getTo(), message);
					
					//envia a próxima pergunta ou retorna menu
					Question nextQuestion = getNextQuestion(user);
					
					if (nextQuestion == null) {
						message = NO_QUESTION_TO_ANSWER;
					} else {
						//salva a questão na sessão
						mapUserData.put("lastQuestion", nextQuestion.getId());
						
						//busca as escolhas pra essa questão
						List<String> choices = new ArrayList<>();
						for (Choice cho : nextQuestion.getChoices()) {
							choices.add(cho.getName());
						}
	
						messageService.sendListMessage(messageDto.getMessage().getTo(), nextQuestion.getName(), choices);
						message = "";
					}
					
				} else {
					message = QUESTION_ANSWERED;
				}
								
			} else {
				message = NOT_UNDERSTAND_MESSAGE;
			}
			if (!message.equals("")) {
				messageService.sendMessage(messageDto.getMessage().getTo(), message);
			}
			
			
			System.out.println(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return"";
	}
	
	private boolean isAnswer(String content) {
		return choiceService.isChoice(content);
	}

	private String getMessageByMenu(MessageEventDto messageDto, String content, User user) {
		if ("1".equals(content)) {
			
			Question question = getNextQuestion(user);
			
			if (question == null) {
				return NO_QUESTION_TO_ANSWER;
			}
			
			//salva a questão na sessão
			Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
			mapUserData.put("lastQuestion", question.getId());
			
			//busca as escolhas pra essa questão
			List<String> choices = new ArrayList<>();
			for (Choice choice : question.getChoices()) {
				choices.add(choice.getName());
			}

			messageService.sendListMessage(messageDto.getMessage().getTo(), question.getName(), choices);
			
			return "";
		}
		return "";
	}

	private Question getNextQuestion(User user) {
		//verifica se usuario tem questionario
		Quiz quiz = userService.findQuizByUser(user);
		
		//busca as questões respondidas
		List<Question> questionsAnswereds = userService.findQuestionAnsweredsByUser(user);
		
		//filtra as questões respondidas com o total e gera a questão
		Question question = filterQuestionsNotAnswered(quiz.getQuestions(), questionsAnswereds);
		return question;
	}

	private Question filterQuestionsNotAnswered(List<Question> questionsTotal, List<Question> questionsAnswereds) {
		
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

	private String getRegistryMessage() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("1) Responder questionário");
		sb.append("\n");
		sb.append("2) Cadastrar projeto");
		sb.append("\n");
		sb.append("3) Menu 3");
		sb.append("\n");
		sb.append("4) Menu 4");
		sb.append("\n");
		sb.append("5) Menu 5");
		sb.append("\n");
		sb.append("6) Menu 6");
		sb.append("\n");
		sb.append("7) Menu 7");
		sb.append("\n");
		
		
		return sb.toString();
	}

	private boolean checkGreeting(String text) {
		return text.toLowerCase().startsWith("oi") || text.toLowerCase().startsWith("ola");
	}
	
	private boolean isRegistry(String text) {
		return text.toLowerCase().startsWith("t") && text.length() == 7;
	}

	private String getContent(MessageEventDto message) {
		
		if (message.getMessage() != null && message.getMessage().getContents() != null && 
				!message.getMessage().getContents().isEmpty()) {
			return message.getMessage().getContents().get(0).getText();
		} 
		return null;
	}
	
	private String getFrom(MessageEventDto message) {
		if (message.getMessage() != null) {
			return message.getMessage().getFrom();
		} 
		return null;
	}

}
