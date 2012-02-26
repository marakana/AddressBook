package addressbook;

import java.io.File;

public class SerializingFileBaseAddressBookFactory implements
		AddressBookFactory {

	private File dir;

	public void setDir(String dir) {
		this.dir = new File(dir);
	}

	@Override
	public AddressBook getAddressBook() {
		return new SerializingFileBaseAddressBook(this.dir);
	}

}
