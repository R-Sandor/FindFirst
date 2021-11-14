package dev.renegade.bookmarkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

@SpringBootApplication
public class BookmarkitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkitApplication.class, args);
	}

	// Bootstrap some test data into the in-memory database
	@Bean
	ApplicationRunner init(BookmarkRepository repository) {
		return args -> {
			Stream.of("Buy milk", "Eat pizza", "Write tutorial", "Study Vue.js", "Go kayaking")
					.forEach(name -> {
						Bookmark bookMark = new Bookmark(name, name);
						System.out.println(bookMark);
						repository.save(bookMark);
					});
			repository.findAll().forEach(System.out::println);
		};

	}

	// Fix the CORS errors
	@Bean
	public FilterRegistrationBean simpleCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// *** URL below needs to match the Vue client URL and port ***
		// Local host and 127.0.0.1 are the same 
		config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://127.0.0.1:8080")); 
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
