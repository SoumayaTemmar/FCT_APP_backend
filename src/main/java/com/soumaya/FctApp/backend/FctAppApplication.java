package com.soumaya.FctApp.backend;

import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
import com.soumaya.FctApp.backend.User.GradeType;
import com.soumaya.FctApp.backend.User.Role;
import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class FctAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FctAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdminUser(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			UniteRepository uniteRepository
	){
		return args -> {
			final String username = "admin.user";
			if (!uniteRepository.existsByDenominationFr("ELIT") && !uniteRepository.existsByImputation("ELIT")){
				Unite unite= Unite.builder()
						.code("ELIT23")
						.imputation("ELIT")
						.denominationFr("ELIT")
						.deleted(false).build();

				uniteRepository.save(unite);
			}
			if (!userRepository.existsByUsername(username)){
				Unite unite = uniteRepository.findByDenominationFrAndDeletedFalse("ELIT")
						.orElseThrow(()-> new EntityNotFoundException("unite does not exist"));

				User adminUser = User.builder()
						.firstName("admin")
						.lastName("user")
						.username(username)
						.mat("123456789")
						.unite(unite)
						.password(passwordEncoder.encode("admin1234"))
						.roles(Set.of(Role.ADMIN))
						.grade(GradeType.RESPONSABLE)
						.enabled(true)
						.accountLocked(false)
						.build();

				userRepository.save(adminUser);
			}
		};
	}
}
