package addressbook.file;

import javax.xml.parsers.ParserConfigurationException;

public class XmlFileBasedAddressBookTest extends
	AbstractFileBasedAddressBookTest {

    @Override
    protected ContactTranscoder getContactTranscoder()
	    throws ParserConfigurationException {
	return new XmlContactTranscoder();
    }

}
