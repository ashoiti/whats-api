package br.com.whats.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class UserQuestionKey implements Serializable {

	private static final long serialVersionUID = 3988920875956015629L;

	@Column(name = "id_user")
    private Integer userId;

    @Column(name = "id_question")
    private Integer questionId;

}
