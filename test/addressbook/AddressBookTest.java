package addressbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ContactTest.class,
		SerializingFileBaseAddressBookTest.class, InMemoryAddressBookTest.class })
public class AddressBookTest {

}
