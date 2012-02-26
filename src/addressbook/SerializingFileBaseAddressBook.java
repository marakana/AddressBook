package addressbook;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.DatatypeConverter;

public class SerializingFileBaseAddressBook implements AddressBook {

	private static final String EXTENSION = ".contact";

	private static final FileFilter CONTACT_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isFile()
					&& file.getName().endsWith(
							SerializingFileBaseAddressBook.EXTENSION);
		}
	};

	private final File dir;

	public SerializingFileBaseAddressBook(File dir) {
		this.dir = dir;
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
	public Contact getByEmail(String email) throws DataAccessException {
		return this.getByFile(this.getFileForEmail(email));
	}

	private Contact getByFile(File file) throws DataAccessException {
		if (!file.exists()) {
			return null;
		} else {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new GZIPInputStream(new FileInputStream(file)));
				try {
					return (Contact) in.readObject();
				} finally {
					in.close();
				}
			} catch (FileNotFoundException e) {
				return null;
			} catch (Exception e) {
				throw new DataAccessException("Cannot get contact from file "
						+ file.getAbsolutePath(), e);
			}
		}
	}

	@Override
	public List<Contact> getAll() throws DataAccessException {
		File[] files = this.dir.listFiles(CONTACT_FILE_FILTER);
		if (files == null) {
			return Collections.emptyList();
		}
		List<Contact> contacts = new ArrayList<Contact>(files.length);
		for (int i = 0; i < files.length; i++) {
			contacts.add(this.getByFile(files[i]));
		}
		// Collections.sort(contacts);
		return contacts;
	}

	@Override
	public void store(Contact contact) throws DataAccessException {
		File file = this.getFileForEmail(contact.getEmail());
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(file)));
			try {
				out.writeObject(contact);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new DataAccessException("Cannot store contact [" + contact
					+ "] to file " + file.getAbsolutePath(), e);
		}
	}

	@Override
	public void deleteByEmail(String email) throws DataAccessException {
		File file = this.getFileForEmail(email);
		if (file.exists() && !file.delete()) {
			throw new DataAccessException("Cannot delete contact with email ["
					+ email + "] in file " + file.getAbsolutePath());
		}
	}

	@Override
	public void close() throws DataAccessException {
	}

}
