package com.testmockito.testmockitoproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.testmockito.testmockitoproject.model.Movie;
import com.testmockito.testmockitoproject.repository.MoviesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {
	
	private final MoviesRepository moviesRepository;
	
	public Movie save(Movie movie) {
		return moviesRepository.save(movie);
	}
	
	public List<Movie> getAllMovies(){
		return moviesRepository.findAll();
	}
	
	public Movie getMovieById(Long id) {
		return moviesRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie found for the id "+id));
	}
    
	public Movie updateMovie(Movie movie, Long id) {
		Movie existingMovie = moviesRepository.findById(id).get();
		existingMovie.setGenera(movie.getGenera());
		existingMovie.setName(movie.getName());
		existingMovie.setReleaseDate(movie.getReleaseDate());
		return moviesRepository.save(existingMovie);
	}
	
	public void deleteMovie(Long id) {
		Movie existingMovie = moviesRepository.findById(id).get();
		moviesRepository.delete(existingMovie);
	}
}
