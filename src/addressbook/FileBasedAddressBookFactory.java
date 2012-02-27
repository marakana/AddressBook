package addressbook;

import java.io.File;

public class FileBasedAddressBookFactory implements AddressBookFactory {

	private File dir;

	private ContactTranscoder contactTranscoder;

	@Default(".")
	public void setDir(String dir) {
		this.dir = new File(dir);
	}

	@Required
	public void setContactTranscoder(String contactTranscoder)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.contactTranscoder = (ContactTranscoder) Class.forName(
				contactTranscoder).newInstance();
	}

	@Override
	public AddressBook getAddressBook() {
		return new FileBasedAddressBook(this.dir, this.contactTranscoder);
	}
}
