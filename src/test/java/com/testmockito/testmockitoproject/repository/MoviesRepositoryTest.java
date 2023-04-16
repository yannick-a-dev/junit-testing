package com.testmockito.testmockitoproject.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.testmockito.testmockitoproject.model.Movie;

//@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase
@SpringBootTest
public class MoviesRepositoryTest {

	@Autowired
	private MoviesRepository moviesRepository;

	private Movie avatarMovie;
	private Movie titanicMovie;

	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

		titanicMovie = new Movie();
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
	}

	@Test
	void save() {
		// Arrange
		
		// Act
		Movie newMovie = moviesRepository.save(avatarMovie);
		// Assert
		assertNotNull(newMovie);
		assertThat(newMovie.getId()).isNotEqualTo(null);
	}

	@Test
	@DisplayName("It should return the movies list with size of 2")
	void getAllMovies() {
		
		moviesRepository.save(avatarMovie);
		moviesRepository.save(titanicMovie);

		List<Movie> list = moviesRepository.findAll();

		assertNotNull(list);
		assertThat(list).isNotNull();
		assertEquals(4, list.size());
	}

	@Test
	@DisplayName("It should return the movie by its id")
	void getMoviesById() {
		moviesRepository.save(avatarMovie);

		Movie existingMovie = moviesRepository.findById(avatarMovie.getId()).get();

		assertNotNull(existingMovie);
		assertEquals("Action", existingMovie.getGenera());
		assertThat(avatarMovie.getReleaseDate()).isBefore(LocalDate.of(2000, Month.APRIL, 23));
	}

	@Test
	@DisplayName("It should update the movie with genera FANTACY")
	void updateMovie() {
		
		moviesRepository.save(avatarMovie);
		Movie existingMovie = moviesRepository.findById(avatarMovie.getId()).get();

		existingMovie.setGenera("Fantacy");
		Movie newMovie = moviesRepository.save(existingMovie);

		assertEquals("Fantacy", newMovie.getGenera());
		assertEquals("Avatar", newMovie.getName());
	}

	@Test
	@DisplayName("It should delete the existing movie")
	void deleteMovie() {
		
		moviesRepository.save(avatarMovie);
		Long id = avatarMovie.getId();

		moviesRepository.save(titanicMovie);

		moviesRepository.delete(avatarMovie);
		Optional<Movie> existingMovie = moviesRepository.findById(id);
		List<Movie> list = moviesRepository.findAll();

		assertEquals(2, list.size());
		assertThat(existingMovie).isEmpty();
	}

	@Test
	@DisplayName("It should return the movies list with genera ROMANCE")
	void getMoviesByGenera() {
		
		moviesRepository.save(avatarMovie);
		moviesRepository.save(titanicMovie);

		List<Movie> list = moviesRepository.findByGenera("Romance");

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(3);

	}
}
