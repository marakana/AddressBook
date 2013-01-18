package addressbook.file;

import java.io.File;

import addressbook.AddressBook;
import addressbook.AddressBookFactory;
import addressbook.Default;
import addressbook.Required;

public class FileBasedAddressBookFactory implements AddressBookFactory {

    private File dir;

    private ContactTranscoder contactTranscoder;

    @Default(".")
    public void setDir(String dir) {
	this.dir = new File(dir);
    }

    @Required
    public void setContactTranscoderType(String contactTranscoderType)
	    throws InstantiationException, IllegalAccessException,
	    ClassNotFoundException {
	this.contactTranscoder = (ContactTranscoder) Class.forName(
		contactTranscoderType).newInstance();
    }

    @Override
    public AddressBook getAddressBook() {
	return new FileBasedAddressBook(this.dir, this.contactTranscoder);
    }
}
