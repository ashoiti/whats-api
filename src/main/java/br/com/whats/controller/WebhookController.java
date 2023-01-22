package br.com.whats.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvia.api.sdk.webhook.MessageEvent;

import br.com.whats.dto.MessageEventDto;
import br.com.whats.model.Choice;
import br.com.whats.model.Project;
import br.com.whats.model.Question;
import br.com.whats.model.Quiz;
import br.com.whats.model.User;
import br.com.whats.service.ChoiceService;
import br.com.whats.service.MessageService;
import br.com.whats.service.ProjectService;
import br.com.whats.service.QuestionService;
import br.com.whats.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/webhook")
public class WebhookController {
	
	private static final String NOT_UNDERSTAND_MESSAGE = "Não entendi o que quis dizer...Diga um oi para continuar";
	private static final String GREETING_MESSAGE = "Olá! Digite a sua matrícula:";
	private static final String USER_NOT_FOUND = "Matrícula não encontrada...Tente novamente";
	private static final String CORRECT_ANSWER = "Parabéns! Você acertou!!!";
	private static final String WRONG_ANSWER = "Não foi dessa vez...A resposta correta é: ";
	private static final String QUESTION_ANSWERED = "Você já respondeu essa pergunta";
	private static final String NO_QUESTION_TO_ANSWER = "Parabéns! Você respondeu todas as perguntas, sua pontuação para este quiz é ";
	private static final String PROJECT_REGISTRATION = "Projeto cadastrado!";
	
	private static Map<String, Map<String, Object>> mapUser = new HashMap<String, Map<String,Object>>();
	
	private static final String[] PROJECT_QUESTION = new String[] { 
			  "Nome do projeto", "Nome da loja", "Período de ativação em loja", 
			  "Alinhamento com as iniciativas estratégicas da Colgate", "Responsável", "Ajudantes", "Descrição",
			  "Inserir imagens", "Clique no clipe ( 📎 ) para anexar as imagens do projeto",
			  "Clique em OK para confirmar a inscrição do seu projeto"};
	
	private static final String[] PROJECT_CHOICES_ALIGNMENT = new String[] { 
			  "Eficiencia e produtividade", "Criatividade e inovação", "Metas e incentivos" };
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChoiceService choiceService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ProjectService projectService;
	
	@PostMapping
	public String login(@RequestBody MessageEvent messageEvent) {
		
		String messageJson = messageEvent.toString();
		
		try {
			MessageEventDto messageDto = new ObjectMapper().readValue(messageJson, MessageEventDto.class);
			String content = getContent(messageDto);
			
			String message = "";
			
			if (isProjectAnswer(messageDto)) {
				Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
				
				int question = (int) mapUserData.get("projectQuestion");
				Project project = (Project) mapUserData.get("project");
				
				switch (question) {
					case 1: {
						project.setName(content);
						mapUserData.put("projectQuestion", 2);
						log.info("Sending to: "+ messageDto.getMessage().getFrom() + ": " + PROJECT_QUESTION[1]);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[1]);
						break;
					} case 2: {
						project.setStore(content);
						mapUserData.put("projectQuestion", 3);
						log.info("Sending to: "+ messageDto.getMessage().getFrom() + ": " + PROJECT_QUESTION[2]);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[2]);
						break;
					} case 3: {
						project.setActivation(content);
						mapUserData.put("projectQuestion", 4);
						messageService.sendListMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[3], Arrays.asList(PROJECT_CHOICES_ALIGNMENT));
						break;
					} case 4: {
						project.setAlignment(content);
						mapUserData.put("projectQuestion", 5);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[4]);
						break;
					} case 5: {
						project.setResponsible(content);
						mapUserData.put("projectQuestion", 6);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[5]);
						break;
					} case 6: {
						project.setAssistant(content);
						mapUserData.put("projectQuestion", 7);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[6]);
						break;
					} case 7: {
						project.setDescription(content);
						mapUserData.put("projectQuestion", 8);
						
						List<String> list = new ArrayList<String>();
						list.add(PROJECT_QUESTION[7]);
						messageService.sendButtonMessage(messageDto.getMessage().getFrom(), "Inserir imagens do projeto", list, false);
						
						break;
					} case 8: {
						mapUserData.put("projectQuestion", 9);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[8]);
						
						break;
					} case 9: {
						mapUserData.put("projectQuestion", 10);
						
						List<String> list = new ArrayList<String>();
						list.add("OK");
						messageService.sendButtonMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[9], list, false);
						
						break;
					} case 10: {
						projectService.save(project);
						messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_REGISTRATION);
						mapUserData.remove("projectQuestion");
					}
					
				}
			} else if (checkGreeting(content)) {
				
				message = GREETING_MESSAGE;
				
			} else if (content.matches("[1-9]")) {
				
				Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
				User user = (User) mapUserData.get("user");
				message = getMessageByMenu(messageDto, content, user);
				
			} else if (isRegistry(content)) {
				
				User findByRegistry = userService.findByRegistry(content.toLowerCase());
				if (findByRegistry != null) {
					boolean isAdmin = findByRegistry.getProfile().equals("ADMIN");
					message = getMenuMessage(findByRegistry.getName(), isAdmin);
					
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
					messageService.sendMessage(messageDto.getMessage().getFrom(), message);
					
					//envia a próxima pergunta ou retorna menu
					Question nextQuestion = questionService.getNextQuestion(user);
					
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
						
						if (nextQuestion.getId() == 2) {
							choices.remove(3);
							messageService.sendButtonMessage(messageDto.getMessage().getFrom(), nextQuestion.getName(), choices, true);
						} else {
							messageService.sendListMessage(messageDto.getMessage().getFrom(), nextQuestion.getName(), choices);
						}
	
						message = "";
					}
					
				} else {
					message = QUESTION_ANSWERED;
				}
								
			} else {
				message = NOT_UNDERSTAND_MESSAGE;
			}
			if (!message.equals("")) {
				messageService.sendMessage(messageDto.getMessage().getFrom(), message);
			}
			
			
			System.out.println(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return"";
	}
	
	private boolean isProjectAnswer(MessageEventDto messageDto) {
		if (mapUser.containsKey(getFrom(messageDto))) {
			Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
			return mapUserData.containsKey("projectQuestion");
		}
		return false;
	}
	
	private boolean isAnswer(String content) {
		return choiceService.isChoice(content);
	}

	private String getMessageByMenu(MessageEventDto messageDto, String content, User user) {
		Map<String, Object> mapUserData = (Map<String, Object>) mapUser.get(getFrom(messageDto));
		if ("1".equals(content)) {
			
			Question question = questionService.getNextQuestion(user);
			
			if (question == null) {
				return NO_QUESTION_TO_ANSWER;
			}
			
			//salva a questão na sessão
			mapUserData.put("lastQuestion", question.getId());
			
			//busca as escolhas pra essa questão
			List<String> choices = new ArrayList<>();
			for (Choice choice : question.getChoices()) {
				choices.add(choice.getName());
			}

			messageService.sendListMessage(messageDto.getMessage().getFrom(), question.getName(), choices);
			
			return "";
		} else if ("2".equals(content)) {
			mapUserData.put("projectQuestion", 1);
			
			Project project = new Project();
			project.setUser((User)mapUserData.get("user"));
			mapUserData.put("project", project);
			
			messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[0]);
		} else if ("8".equals(content)) {
			StringBuilder sb = new StringBuilder();
			sb.append("Escolha uma das opções abaixo:");
			sb.append("\n");
			sb.append("1) Menu admin 1");
			sb.append("\n");
			sb.append("2) Menu admin 2");
			
			messageService.sendMessage(messageDto.getMessage().getFrom(), PROJECT_QUESTION[0]);
		}
		return "";
	}

	private String getMenuMessage(String name, boolean isAdmin) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Olá " + name + "!. Escolha uma das opções abaixo:");
		sb.append("\n");
		sb.append("1) Quiz");
		sb.append("\n");
		sb.append("2) Inscrição / votação de projetos");
		sb.append("\n");
		sb.append("3) Metas SOE");
		sb.append("\n");
		sb.append("4) Metas SOS");
		sb.append("\n");
		sb.append("5) Desafio adicional");
		sb.append("\n");
		sb.append("6) Ranking");
		sb.append("\n");
		sb.append("7) FAQ (Duvidas Frequentes)");
		sb.append("\n");
		sb.append("8) Regulamento da Campanha");
		sb.append("\n");
		sb.append("9) Falar com suporte");
		sb.append("\n");
		if (isAdmin) {
			sb.append("10) Administração");
			sb.append("\n");
		}
		
		
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
