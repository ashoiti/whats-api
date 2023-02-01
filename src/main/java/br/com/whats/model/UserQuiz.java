package br.com.whats.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="tb_user_quiz")
public class UserQuiz {
	
	@EmbeddedId
    private UserQuizKey id;

	@ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "id_quiz")
    private Quiz quiz;
    
    private Integer answered;
    
    private Integer result;

}
