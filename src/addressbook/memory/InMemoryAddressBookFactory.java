package addressbook.memory;

import addressbook.AddressBook;
import addressbook.AddressBookFactory;

public class InMemoryAddressBookFactory implements AddressBookFactory {

    @Override
    public AddressBook getAddressBook() {
	return new InMemoryAddressBook();
    }

}
