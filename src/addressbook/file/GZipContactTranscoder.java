package addressbook.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import addressbook.Contact;

public class GZipContactTranscoder implements ContactTranscoder {

    private final ContactTranscoder contactTranscoder;

    public GZipContactTranscoder(ContactTranscoder contactTranscoder) {
	this.contactTranscoder = contactTranscoder;
    }

    @Override
    public Contact decode(InputStream in) throws IOException,
	    ContactTranscodingException {
	GZIPInputStream gIn = new GZIPInputStream(in);
	try {
	    return this.contactTranscoder.decode(gIn);
	} finally {
	    gIn.close();
	}
    }

    @Override
    public void encode(Contact contact, OutputStream out) throws IOException,
	    ContactTranscodingException {
	GZIPOutputStream gOut = new GZIPOutputStream(out);
	this.contactTranscoder.encode(contact, gOut);
	gOut.close();
    }

}
