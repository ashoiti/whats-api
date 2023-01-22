package br.com.whats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.whats.model.Image;

public interface IImageRepository extends JpaRepository<Image, Integer> {


}
