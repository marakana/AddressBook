package addressbook;

public class InMemoryAddressBookFactory implements AddressBookFactory {

	@Override
	public AddressBook getAddressBook() {
		return new InMemoryAddressBook();
	}

}
