package cl.springmachine.file.storage.spring.boot.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import cl.springmachine.file.storage.spring.boot.exceptions.CustomException;

public interface FileStorageService {

	void init() throws CustomException;

	void save(MultipartFile file) throws CustomException;

	Resource load(String filename) throws CustomException;

	void deleteAll();

	Stream<Path> loadAll() throws CustomException;

}
