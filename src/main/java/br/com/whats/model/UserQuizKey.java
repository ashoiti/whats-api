package br.com.whats.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class UserQuizKey implements Serializable {

	private static final long serialVersionUID = 5331758145507024903L;
	
	@Column(name = "id_user")
    private Integer userId;

    @Column(name = "id_quiz")
    private Integer quizId;

}
