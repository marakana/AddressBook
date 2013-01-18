package addressbook.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import addressbook.AddressBook;
import addressbook.AddressBookException;
import addressbook.AddressBookFactory;
import addressbook.Default;
import addressbook.Required;

public class RemoteAddressBookClientFactory implements AddressBookFactory {

    private String registryHost;
    private int registryPort;
    private String name;

    @Required
    @Default("localhost")
    public void setRegistryHost(String registryHost) {
	this.registryHost = registryHost;
    }

    @Required
    @Default("1099")
    public void setRegistryPort(String registryPort) {
	this.registryPort = Integer.parseInt(registryPort);
    }

    @Required
    @Default("RemoteAddressBook")
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public AddressBook getAddressBook() throws AddressBookException {
	try {
	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	    }
	    Registry registry = LocateRegistry.getRegistry(this.registryHost,
		    this.registryPort);
	    RemoteAddressBook remoteAddressBook = (RemoteAddressBook) registry
		    .lookup(name);
	    return new RemoteAddressBookClient(remoteAddressBook);
	} catch (RemoteException e) {
	    throw new AddressBookException(
		    "Failed to connect to the remote registry", e);
	} catch (NotBoundException e) {
	    throw new AddressBookException("Failed to look up server by name ["
		    + name + "]", e);
	} catch (ClassCastException e) {
	    throw new AddressBookException(
		    "While looking up a server by name [" + name
			    + "] expecting ["
			    + RemoteAddressBook.class.getName() + "]", e);
	}
    }

}
