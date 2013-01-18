package addressbook.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import addressbook.AddressBook;
import addressbook.AddressBookException;
import addressbook.AddressBookFactory;
import addressbook.Contact;

public class RemoteAddressBookServer implements RemoteAddressBook {

    private final AddressBook addressBook;

    public RemoteAddressBookServer(AddressBook addressBook) {
	this.addressBook = addressBook;
    }

    @Override
    public Contact getByEmail(String email) throws AddressBookException,
	    RemoteException {
	return this.addressBook.getByEmail(email);
    }

    @Override
    public List<Contact> getAll() throws AddressBookException, RemoteException {
	return this.addressBook.getAll();
    }

    @Override
    public void store(Contact contact) throws AddressBookException,
	    RemoteException {
	this.addressBook.store(contact);
    }

    @Override
    public void deleteByEmail(String email) throws AddressBookException,
	    RemoteException {
	this.deleteByEmail(email);
    }

    @Override
    public void shutdown() throws AddressBookException, RemoteException {
	this.addressBook.close();
    }

    private static final String USAGE = "RemoteAddressBookServer <name> <address-book-factory-type> <prop-name>=<prop-value> ...";

    public static void main(String[] args) {
	if (args.length < 2) {
	    System.err.println(USAGE);
	    return;
	}

	final String name = args[0];

	try {
	    AddressBookFactory.Builder builder = new AddressBookFactory.Builder(
		    args[1]);
	    for (int i = 2; i < args.length; i++) {
		try {
		    builder.setProperty(args[i]);
		} catch (IllegalArgumentException e) {
		    System.err.println("ERROR: " + e.getMessage());
		    System.err.println(USAGE);
		    return;
		}
	    }
	    AddressBook addressBook = builder.getAddressBookFactory()
		    .getAddressBook();

	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	    }

	    final Registry registry = LocateRegistry.createRegistry(1099);
	    // LocateRegistry.getRegistry("127.0.0.1");

	    // try {
	    // registry.unbind(name);
	    // } catch (Exception e) {
	    // throw new AddressBookException("Failed to unbind", e);
	    // }

	    RemoteAddressBookServer server = new RemoteAddressBookServer(
		    addressBook);

	    RemoteAddressBook stub = (RemoteAddressBook) UnicastRemoteObject
		    .exportObject(server, 0);
	    registry.rebind(name, stub);
	    System.out.println("Bound as " + name);
	} catch (RemoteException e) {
	    System.err.println("Failed to bind as " + name);
	    e.printStackTrace();
	} catch (AddressBookException e) {
	    System.err.println("Failed to get to the address book");
	    e.printStackTrace();
	}
    }
}
