package com.testmockito.testmockitoproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testmockito.testmockitoproject.model.Movie;

public interface MoviesRepository extends JpaRepository<Movie, Long> {

	List<Movie> findByGenera(String genera);
}
