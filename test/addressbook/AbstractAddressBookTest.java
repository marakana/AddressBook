package addressbook;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractAddressBookTest {

    private AddressBook addressBook;

    @Before
    public void initAddressBook() throws Exception {
	this.addressBook = getAddressBook();
    }

    protected abstract AddressBook getAddressBook() throws Exception;

    private void store(Contact c1) throws AddressBookException {
	addressBook.store(c1);
	Contact c2 = addressBook.getByEmail(c1.getEmail());
	assertEquals(c1, c2);
	assertEquals(c1.getEmail(), c2.getEmail());
	assertEquals(c1.getFirstName(), c2.getFirstName());
	assertEquals(c1.getLastName(), c2.getLastName());
	assertEquals(c1.getPhone(), c2.getPhone());
    }

    @Test
    public void store() throws AddressBookException {
	store(new Contact("John", "Smith", "john@smith.com"));
    }

    @Test
    public void storeWithPhone() throws AddressBookException {
	store(ContactBuilder.build("john@smith.com").withFirstName("John")
		.withLastName("Smith").withPhone("415-555-1234").getContact());
    }

    @Test
    public void storeWithNullFirstName() throws AddressBookException {
	store(ContactBuilder.build("john@smith.com").withFirstName(null)
		.withLastName("Smith").withPhone("415-555-1234").getContact());
    }

    @Test
    public void storeWithNullLastName() throws AddressBookException {
	store(ContactBuilder.build("john@smith.com").withFirstName("John")
		.withLastName(null).withPhone("415-555-1234").getContact());
    }

    @Test(expected = NullPointerException.class)
    public void storeNull() throws AddressBookException {
	this.addressBook.store(null);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNull() throws AddressBookException {
	this.addressBook.deleteByEmail(null);
    }

    @After
    public void destroyAddressBook() {
	this.addressBook = null;
    }
}
