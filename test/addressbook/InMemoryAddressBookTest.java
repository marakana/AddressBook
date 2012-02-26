package addressbook;

public class InMemoryAddressBookTest extends AbstractAddressBookTest {

	@Override
	protected AddressBook getAddressBook() {
		return new InMemoryAddressBook();
	}

}
