package addressbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import addressbook.file.AddressBookFileTestSuite;
import addressbook.memory.AddressBookMemoryTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ContactTest.class, AddressBookFileTestSuite.class,
	AddressBookMemoryTestSuite.class })
public class AddressBookTestSuite {

}
