package addressbook;

public class ContactTranscodingException extends AddressBookException {

	private static final long serialVersionUID = 6899423532082458330L;

	public ContactTranscodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContactTranscodingException(String message) {
		super(message);
	}
}
