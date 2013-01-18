package addressbook.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import addressbook.AddressBookException;
import addressbook.Contact;

public interface RemoteAddressBook extends Remote {

    public Contact getByEmail(String email) throws AddressBookException,
	    RemoteException;

    public List<Contact> getAll() throws AddressBookException, RemoteException;

    public void store(Contact contact) throws AddressBookException,
	    RemoteException;

    public void deleteByEmail(String email) throws AddressBookException,
	    RemoteException;

    public void shutdown() throws AddressBookException, RemoteException;
}
