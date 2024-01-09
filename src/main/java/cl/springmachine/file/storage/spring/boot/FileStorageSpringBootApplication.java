package cl.springmachine.file.storage.spring.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cl.springmachine.file.storage.spring.boot.services.FileStorageService;
import jakarta.annotation.Resource;

@SpringBootApplication
public class FileStorageSpringBootApplication implements CommandLineRunner {

	@Resource
	FileStorageService fileStorageService;

	public static void main(String[] args) {
		SpringApplication.run(FileStorageSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorageService.init();

	}

}
