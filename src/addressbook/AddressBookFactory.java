package addressbook;

/**
 * Factory for building address book. Must have a public no-arg constructor.
 * Properties for the factory will be passed via the setter methods that take a
 * single string paramter.
 * 
 * @author sasa
 * 
 */
public interface AddressBookFactory {
	public AddressBook getAddressBook() throws DataAccessException;
}
