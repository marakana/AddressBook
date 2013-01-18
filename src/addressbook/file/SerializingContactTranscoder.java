package addressbook.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import addressbook.Contact;

public class SerializingContactTranscoder implements ContactTranscoder {

    @Override
    public Contact decode(InputStream in) throws ContactTranscodingException,
	    IOException {
	try {
	    return (Contact) new ObjectInputStream(in).readObject();
	} catch (ClassNotFoundException e) {
	    throw new ContactTranscodingException(
		    "Encountered an unexpected object type while decoding contact",
		    e);
	}
    }

    @Override
    public void encode(Contact contact, OutputStream out) throws IOException {
	new ObjectOutputStream(out).writeObject(contact);
    }
}
