package addressbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAddressBook implements AddressBook {

	private final Map<String, Contact> contacts = new HashMap<String, Contact>();

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		return this.contacts.get(email);
	}

	@Override
	public List<Contact> getAll() throws AddressBookException {
		List<Contact> result = new ArrayList<Contact>(this.contacts.values());
		Collections.sort(result);
		return result;
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		this.contacts.put(contact.getEmail(), contact);
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		if (email == null) {
			throw new NullPointerException("Email must not be null");
		}
		this.contacts.remove(email);
	}

	@Override
	public void close() throws AddressBookException {

	}

}
