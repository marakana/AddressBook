package addressbook.rmi;

import java.rmi.RemoteException;
import java.util.List;

import addressbook.AddressBook;
import addressbook.AddressBookException;
import addressbook.Contact;

public class RemoteAddressBookClient implements AddressBook {

    private final RemoteAddressBook remoteAddressBook;

    public RemoteAddressBookClient(RemoteAddressBook remoteAddressBook) {
	this.remoteAddressBook = remoteAddressBook;
    }

    @Override
    public Contact getByEmail(String email) throws AddressBookException {
	try {
	    return this.remoteAddressBook.getByEmail(email);
	} catch (RemoteException e) {
	    throw new AddressBookException(
		    "Encountered an error while getting contact by email ["
			    + email + "] from the remote server", e);
	}
    }

    @Override
    public List<Contact> getAll() throws AddressBookException {
	try {
	    return this.remoteAddressBook.getAll();
	} catch (RemoteException e) {
	    throw new AddressBookException(
		    "Encountered an error while getting all contacts from the remote server",
		    e);
	}
    }

    @Override
    public void store(Contact contact) throws AddressBookException {
	try {
	    this.remoteAddressBook.store(contact);
	} catch (RemoteException e) {
	    throw new AddressBookException(
		    "Encountered an error while storing contact [" + contact
			    + "] on the remote server", e);
	}
    }

    @Override
    public void deleteByEmail(String email) throws AddressBookException {
	try {
	    this.remoteAddressBook.deleteByEmail(email);
	} catch (RemoteException e) {
	    throw new AddressBookException(
		    "Encountered an error while deleting contact by email ["
			    + email + "] on the remote server", e);
	}
    }

    @Override
    public void close() throws AddressBookException {
	// try {
	// this.remoteAddressBook.shutdown();
	// } catch (RemoteException e) {
	// throw new AddressBookException(
	// "Encountered an error while shutting down the remote server",
	// e);
	// }
    }
}
