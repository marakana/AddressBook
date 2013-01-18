package addressbook;

public class AddressBookException extends Exception {

    private static final long serialVersionUID = -5404157887713123901L;

    public AddressBookException(String message) {
	super(message);
    }

    public AddressBookException(String message, Throwable cause) {
	super(message, cause);
    }

}
