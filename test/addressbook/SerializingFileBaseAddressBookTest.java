package addressbook;

import java.io.File;

import org.junit.After;
import org.junit.Before;

public class SerializingFileBaseAddressBookTest extends AbstractAddressBookTest {

	private File dir;

	@Before
	public void initDir() {
		dir = new File(new File(System.getProperty("java.io.tmpdir")),
				String.valueOf(System.currentTimeMillis()));
		dir.mkdirs();
	}

	@After
	public void cleanUpDir() {
		for (File file : dir.listFiles()) {
			file.delete();
		}
		dir.delete();
	}

	@Override
	protected AddressBook getAddressBook() {
		return new SerializingFileBaseAddressBook(dir);
	}
}
