package com.testmockito.testmockitoproject.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import com.testmockito.testmockitoproject.model.Movie;
import com.testmockito.testmockitoproject.repository.MoviesRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesIntegrationTest {

	@LocalServerPort
	private int port;
	
	private String baseUrl = "http://localhost";
	
	private static RestTemplate restTemplate;
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@Autowired
	private MoviesRepository moviesRepository;
	
	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}
	@BeforeEach
	public void beforeSetup() {
		baseUrl = baseUrl + ":" +port + "/movies";
		
		avatarMovie = new Movie();
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

		titanicMovie = new Movie();
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
		
		avatarMovie = moviesRepository.save(avatarMovie);
		titanicMovie = moviesRepository.save(titanicMovie);
	}
	
	@AfterEach
	public void afterSetup() {
		moviesRepository.deleteAll();
	}
	
	@Test
	void shouldCreateMovieTest() {
		Movie avatarMovie = new Movie();
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));
		
		Movie newMovie = restTemplate.postForObject(baseUrl, avatarMovie, Movie.class);
		
		assertNotNull(newMovie);
		assertThat(newMovie.getId()).isNotNull();
	}
	
	void shouldFetchMoviesTest() {
		
		
		List<Movie> list = restTemplate.getForObject(baseUrl, List.class);
		
		assertThat(list.size()).isEqualTo(2);
	}
	
	void shouldFetchOneMovieTest() {
		
		Movie existingMovie = restTemplate.getForObject(baseUrl+"/"+avatarMovie.getId(), Movie.class);
		
		assertNotNull(existingMovie);
		assertEquals("Avatar", existingMovie.getName());
	}
	
	void shouldDeleteMovieTest() {
		
		restTemplate.delete(baseUrl+"/"+avatarMovie.getId());
		
		int count = moviesRepository.findAll().size();
		
		assertEquals(1, count);
	}
	
	@Test
	void shouldUpdateMovieTest() {
		
		avatarMovie.setGenera("Fantacy");
		
		restTemplate.put(baseUrl+"/{id}", avatarMovie, avatarMovie.getId());
		
		Movie existingMovie = restTemplate.getForObject(baseUrl+"/"+avatarMovie.getId(), Movie.class);
		
		assertNotNull(existingMovie);
		assertEquals("Fantacy", existingMovie.getGenera());
	}
}
