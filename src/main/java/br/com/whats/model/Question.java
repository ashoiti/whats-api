package br.com.whats.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="tb_question")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer answer;
    private Integer points;
    
    @ManyToOne
    @JoinColumn(name="id_quiz", nullable=false)	
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question")
    private List<Choice> choices;
	
}