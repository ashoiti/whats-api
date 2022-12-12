package br.com.whats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.User;

public interface IUserRepository extends JpaRepository<User, Integer> {
	
	User findByRegistry(String registry);

}
