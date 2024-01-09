package cl.springmachine.file.storage.spring.boot.exceptions;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionHandler {

	private static final String RFC_STRING = "https://www.rfc-editor.org/rfc/rfc7807";

	@ExceptionHandler(CustomException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ProblemDetail globalExceptionHandler(Exception ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getMessage());
		problemDetail.setType(URI.create(RFC_STRING));
		return problemDetail;
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public ProblemDetail handleException(Exception ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
		problemDetail.setType(URI.create(RFC_STRING));
		return problemDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
		String errorMessages = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		String body = "Validation error: " + errorMessages;
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, body);
		problemDetail.setType(URI.create(RFC_STRING));
		return problemDetail;
	}

}
