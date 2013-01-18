package addressbook.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import addressbook.Contact;

public class XmlContactTranscoder implements ContactTranscoder {

    private static final String CONTACT = "contact";
    private static final String FIRST_NAME = "first-name";
    private static final String LAST_NAME = "last-name";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String ENCODING = "UTF-8";
    private static final byte[] XML_HEADER = ("<?xml version=\"1.0\" encoding=\""
	    + ENCODING + "\"?>\n").getBytes();

    public static void printXmlHeader(OutputStream out) throws IOException {
	out.write(XML_HEADER);
    }

    private final DocumentBuilder builder;

    public XmlContactTranscoder() throws ParserConfigurationException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	this.builder = factory.newDocumentBuilder();
    }

    @Override
    public Contact decode(InputStream in) throws IOException,
	    ContactTranscodingException {
	try {
	    Document document = this.builder.parse(in);
	    Node contactNode = findNodeByName(document, CONTACT);

	    String firstName = getText(findNodeByName(contactNode, FIRST_NAME));
	    String lastName = getText(findNodeByName(contactNode, LAST_NAME));
	    String email = getText(findNodeByName(contactNode, EMAIL));
	    String phone = getText(findNodeByName(contactNode, PHONE));

	    Contact contact = new Contact(firstName, lastName, email);
	    contact.setPhone(phone);
	    return contact;
	} catch (SAXException e) {
	    throw new ContactTranscodingException(
		    "Parsersing error while loading contact from stream", e);
	}
    }

    @Override
    public void encode(Contact contact, OutputStream out) throws IOException,
	    ContactTranscodingException {
	PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,
		ENCODING));
	this.encode(contact, writer);
    }

    public void encode(Contact contact, PrintWriter writer) throws IOException,
	    ContactTranscodingException {
	this.encode(contact, writer, 0);
    }

    public void encode(Contact contact, PrintWriter writer, int indentLevel)
	    throws IOException, ContactTranscodingException {
	this.indent(writer, indentLevel);
	writer.append("<").append(CONTACT).append(">\n");
	printXmlElement(FIRST_NAME, contact.getFirstName(), writer, indentLevel);
	printXmlElement(LAST_NAME, contact.getLastName(), writer, indentLevel);
	printXmlElement(EMAIL, contact.getEmail(), writer, indentLevel);
	printXmlElement(PHONE, contact.getPhone(), writer, indentLevel);
	this.indent(writer, indentLevel);
	writer.append("</").append(CONTACT).append(">\n");
	writer.flush();
	if (writer.checkError()) {
	    throw new IOException(
		    "Encountered an error while writing contact [" + contact
			    + "]");
	}
    }

    private Node findNodeByName(Node node, String name) {
	if (node.getNodeName().equals(name)) {
	    return node;
	} else {
	    for (Node n = node.getFirstChild(); n != null; n = n
		    .getNextSibling()) {
		Node found = findNodeByName(n, name);
		if (found != null) {
		    return found;
		}
	    }
	    return null;
	}
    }

    private String getText(Node node) {
	return node == null ? null : node.getTextContent();
    }

    private static final String[][] ESCAPE = { { "&", "&amp;" },
	    { "<", "&lt;" }, { ">", "&gt;" } };

    private void printXmlElement(String name, String value, PrintWriter writer,
	    int indentLevel) {
	if (value != null) {
	    this.indent(writer, indentLevel);
	    writer.print("\t<");
	    writer.print(name);
	    writer.print('>');
	    for (String[] escape : ESCAPE) {
		value = value.replaceAll(escape[0], escape[1]);
	    }
	    writer.print(value);
	    writer.print("</");
	    writer.print(name);
	    writer.print(">\n");
	}
    }

    private void indent(PrintWriter writer, int level) {
	for (int i = 0; i < level; i++) {
	    writer.write('\t');
	}
    }
}
