package addressbook.file;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import addressbook.AbstractAddressBookTest;
import addressbook.AddressBook;

public abstract class AbstractFileBasedAddressBookTest extends
	AbstractAddressBookTest {

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

    protected abstract ContactTranscoder getContactTranscoder()
	    throws Exception;

    @Override
    protected AddressBook getAddressBook() throws Exception {
	return new FileBasedAddressBook(this.dir, this.getContactTranscoder());
    }
}
