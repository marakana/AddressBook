package addressbook;

import java.net.Socket;

public class AddressBookClientFactory implements AddressBookFactory {

	private int port = 5000;
	private String host;

	@Default("5000")
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}

	@Required
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public AddressBook getAddressBook() throws AddressBookException {
		try {
			return new AddressBookClient(new Socket(host, port));
		} catch (Exception e) {
			throw new AddressBookException("Failed to init socket", e);
		}
	}

}
