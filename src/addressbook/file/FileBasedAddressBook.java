package addressbook.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import addressbook.AddressBook;
import addressbook.AddressBookException;
import addressbook.Contact;

public class FileBasedAddressBook implements AddressBook {

    private static final String EXTENSION = ".contact";

    private static final FileFilter CONTACT_FILE_FILTER = new FileFilter() {
	@Override
	public boolean accept(File file) {
	    return file.isFile()
		    && file.getName().endsWith(FileBasedAddressBook.EXTENSION);
	}
    };

    private final File dir;

    private final ContactTranscoder contactTranscoder;

    public FileBasedAddressBook(File dir, ContactTranscoder contactTranscoder) {
	this.dir = dir;
	this.contactTranscoder = contactTranscoder;
    }

    private File getFileForEmail(String email) {
	try {
	    return new File(this.dir, DatatypeConverter.printHexBinary(email
		    .getBytes("UTF-8")) + EXTENSION);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("Unexpected", e);
	}
    }

    @Override
    public Contact getByEmail(String email) throws AddressBookException {
	return this.getByFile(this.getFileForEmail(email));
    }

    private Contact getByFile(File file) throws AddressBookException {
	if (!file.exists()) {
	    return null;
	} else {
	    try {
		InputStream in = new FileInputStream(file);
		try {
		    return this.contactTranscoder.decode(in);
		} finally {
		    in.close();
		}
	    } catch (FileNotFoundException e) {
		return null;
	    } catch (IOException e) {
		throw new AddressBookException(
			"Failed to get contact from file "
				+ file.getAbsolutePath(), e);
	    }
	}
    }

    @Override
    public List<Contact> getAll() throws AddressBookException {
	File[] files = this.dir.listFiles(CONTACT_FILE_FILTER);
	if (files == null) {
	    return Collections.emptyList();
	}
	List<Contact> contacts = new ArrayList<Contact>(files.length);
	for (int i = 0; i < files.length; i++) {
	    contacts.add(this.getByFile(files[i]));
	}
	Collections.sort(contacts);
	return contacts;
    }

    @Override
    public void store(Contact contact) throws AddressBookException {
	File file = this.getFileForEmail(contact.getEmail());
	try {
	    OutputStream out = new FileOutputStream(file);
	    try {
		this.contactTranscoder.encode(contact, out);
	    } finally {
		out.close();
	    }
	} catch (IOException e) {
	    throw new AddressBookException("Failed to store contact ["
		    + contact + "] to file " + file.getAbsolutePath(), e);
	}
    }

    @Override
    public void deleteByEmail(String email) throws AddressBookException {
	File file = this.getFileForEmail(email);
	if (file.exists() && !file.delete()) {
	    throw new AddressBookException("Cannot delete contact with email ["
		    + email + "] in file " + file.getAbsolutePath());
	}
    }

    @Override
    public void close() throws AddressBookException {
    }

}
