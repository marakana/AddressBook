package addressbook.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import addressbook.Contact;

public interface ContactTranscoder {
    public Contact decode(InputStream in) throws IOException,
	    ContactTranscodingException;

    public void encode(Contact contact, OutputStream out) throws IOException,
	    ContactTranscodingException;
}
