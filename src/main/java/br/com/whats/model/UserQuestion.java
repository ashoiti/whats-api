package br.com.whats.model;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="tb_user_question")
public class UserQuestion {
	
	@EmbeddedId
    private UserQuestionKey id;

	@ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "id_question")
    private Question question;
    
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name="id_choice", nullable=false)	
    private Choice answer;
    
    

}
