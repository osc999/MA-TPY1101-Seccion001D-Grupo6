package com.conectatarot.backend;

import com.conectatarot.backend.entity.Rol;
import com.conectatarot.backend.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RolRepository rolRepository) {
		return args -> {
			createRoleIfMissing(rolRepository, "CLIENTE");
			createRoleIfMissing(rolRepository, "TAROTISTA");
		};
	}

	private void createRoleIfMissing(RolRepository rolRepository, String nombreRol) {
		rolRepository.findByNombreRol(nombreRol).orElseGet(() -> {
			Rol rol = new Rol();
			rol.setNombreRol(nombreRol);
			return rolRepository.save(rol);
		});
	}
}
