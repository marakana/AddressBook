package addressbook.file;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ XmlFileBasedAddressBookTest.class,
	SerializingFileBasedAddressBookTest.class })
public class AddressBookFileTestSuite {

}
