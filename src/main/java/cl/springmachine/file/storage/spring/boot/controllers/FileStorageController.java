package cl.springmachine.file.storage.spring.boot.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import cl.springmachine.file.storage.spring.boot.exceptions.CustomException;
import cl.springmachine.file.storage.spring.boot.models.FileInfoDto;
import cl.springmachine.file.storage.spring.boot.models.ResponseMessageDto;
import cl.springmachine.file.storage.spring.boot.services.FileStorageService;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(value = "*")
@RequiredArgsConstructor
public class FileStorageController {

	private final FileStorageService fileStorageService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessageDto> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			fileStorageService.save(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessageDto(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfoDto>> getListFiles() throws CustomException {
		List<FileInfoDto> fileInfos = fileStorageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileStorageController.class, "getFile", path.getFileName().toString()).build()
					.toString();

			return new FileInfoDto(filename, url);
		}).toList();

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws CustomException {
		Resource file = fileStorageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
