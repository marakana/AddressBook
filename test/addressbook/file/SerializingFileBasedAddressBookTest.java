package addressbook.file;

import javax.xml.parsers.ParserConfigurationException;

public class SerializingFileBasedAddressBookTest extends
	AbstractFileBasedAddressBookTest {

    @Override
    protected ContactTranscoder getContactTranscoder()
	    throws ParserConfigurationException {
	return new SerializingContactTranscoder();
    }

}
