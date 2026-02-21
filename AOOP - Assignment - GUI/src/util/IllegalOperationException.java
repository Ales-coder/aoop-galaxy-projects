package util;

public class IllegalOperationException extends Exception {
	private static final long serialVersionUID = 1490549809776619545L;

	public IllegalOperationException() {
		super();
	}

	public IllegalOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationException(String message) {
		super(message);
	}

	public IllegalOperationException(Throwable cause) {
		super(cause);
	}
}
