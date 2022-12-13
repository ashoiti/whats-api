package br.com.whats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.Project;

public interface IProjectRepository extends JpaRepository<Project, Integer> {
	
	
}
