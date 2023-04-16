package com.testmockito.testmockitoproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.testmockito.testmockitoproject.model.Movie;
import com.testmockito.testmockitoproject.repository.MoviesRepository;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

	@InjectMocks
	private MovieService movieService;
	
	@Mock
	private MoviesRepository moviesRepository;
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

		titanicMovie = new Movie();
		titanicMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
	}
	
	@Test
	@DisplayName("Should save the movie object to database")
	void save() {
		
		when(moviesRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		
		Movie newMovie = movieService.save(avatarMovie);
		
		assertNotNull(newMovie);
		assertThat(newMovie.getName()).isEqualTo("Avatar");
	}
	
	@Test
	@DisplayName("Should return list of movies with size 2")
	void getMovies() {
		
		List<Movie> list = new ArrayList<>();
		list.add(titanicMovie);
		list.add(avatarMovie);
		
		when(moviesRepository.findAll()).thenReturn(list);
		
		List<Movie> movies = movieService.getAllMovies();
		
		assertEquals(2, movies.size());
		assertNotNull(movies);
	}
	
	@Test
	@DisplayName("Should return the Movie object")
	void getMovieById() {
		
		when(moviesRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		
		Movie existingMovie = movieService.getMovieById(1L);
		
		assertNotNull(existingMovie);
		assertThat(existingMovie.getId()).isEqualTo(1L);
	}
	
	@Test
	@DisplayName("Should return throw the Exception")
	void getMovieByIdForException() {
		
		when(moviesRepository.findById(1L)).thenReturn(Optional.of(avatarMovie));
		
		assertThrows(RuntimeException.class, () -> {
			movieService.getMovieById(2L);
		});
	}
	
	@Test
	@DisplayName("Should update the movie into the database")
	void updateMovie() {
		
		when(moviesRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		when(moviesRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		avatarMovie.setGenera("Fantacy");
		
		Movie updatedMovie = movieService.updateMovie(avatarMovie, 1L);
		
		assertNotNull(updatedMovie);
		assertEquals("Fantacy", updatedMovie.getGenera());
	}
	
	@Test
	@DisplayName("Should delete the movie from database")
	void deleteMovie() {
		
		when(moviesRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		doNothing().when(moviesRepository).delete(any(Movie.class));
		
		movieService.deleteMovie(1L);
		
		verify(moviesRepository, times(1)).delete(avatarMovie);
	}
}
