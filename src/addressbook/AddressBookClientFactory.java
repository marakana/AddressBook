package addressbook;

import java.net.Socket;

public class AddressBookClientFactory implements AddressBookFactory {

	private int port = 5000;
	private String host;

	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}

	@Required(defaultValue = "localhost")
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public AddressBook getAddressBook() throws DataAccessException {
		try {
			return new AddressBookClient(new Socket(host, port));
		} catch (Exception e) {
			throw new DataAccessException("Failed to init socket", e);
		}
	}

}
