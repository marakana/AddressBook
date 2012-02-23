package addressbook;

public class DataAccessException extends Exception {

	private static final long serialVersionUID = -5404157887713123901L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
