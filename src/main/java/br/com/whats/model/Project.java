package br.com.whats.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="tb_project")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String store;
    private String activation;
    private String alignment;
    private String responsible;
    private String assistant;
    private String description;
    private String mail;
    
    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)	
    private User user;
    
    @ManyToOne
    @JoinColumn(name="id_image")	
    private Image image;
}