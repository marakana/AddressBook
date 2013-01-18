package addressbook.memory;

import addressbook.AbstractAddressBookTest;
import addressbook.AddressBook;
import addressbook.memory.InMemoryAddressBook;

public class InMemoryAddressBookTest extends AbstractAddressBookTest {

    @Override
    protected AddressBook getAddressBook() {
	return new InMemoryAddressBook();
    }

}
