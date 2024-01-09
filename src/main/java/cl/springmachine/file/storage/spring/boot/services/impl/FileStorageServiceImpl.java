package cl.springmachine.file.storage.spring.boot.services.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import cl.springmachine.file.storage.spring.boot.exceptions.CustomException;
import cl.springmachine.file.storage.spring.boot.services.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path root = Paths.get("uploads");

	@Override
	public void init() throws CustomException {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new CustomException("Could Not Initialize");
		}
	}

	@Override
	public void save(MultipartFile file) throws CustomException {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (FileAlreadyExistsException e) {
			throw new CustomException("File Exists");
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}

	@Override
	public Resource load(String filename) throws CustomException {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new CustomException("File Storage Error");
			}
		} catch (MalformedURLException e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());

	}

	@Override
	public Stream<Path> loadAll() throws CustomException {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new CustomException("File Storage Error");
		}
	}

}
