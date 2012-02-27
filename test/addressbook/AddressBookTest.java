package addressbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ContactTest.class,
		FileBasedAddressBookTest.class, InMemoryAddressBookTest.class })
public class AddressBookTest {

}
