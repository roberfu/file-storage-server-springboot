package cl.springmachine.file.storage.spring.boot.exceptions;

public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
		super(message);
	}
}
