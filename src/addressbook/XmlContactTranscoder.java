package addressbook;

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

public class XmlContactTranscoder implements ContactTranscoder {

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
			Node contactNode = findNodeByName(document, "contact");
			Contact contact = new Contact(findNodeByName(contactNode,
					"first-name").getTextContent(), findNodeByName(contactNode,
					"last-name").getTextContent(), findNodeByName(contactNode,
					"email").getTextContent());
			Node phoneNode = findNodeByName(contactNode, "phone");
			if (phoneNode != null) {
				contact.setPhone(phoneNode.getTextContent());
			}
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
		writer.println("<contact>");
		printXmlElement("first-name", contact.getFirstName(), writer,
				indentLevel);
		printXmlElement("last-name", contact.getLastName(), writer, indentLevel);
		printXmlElement("email", contact.getEmail(), writer, indentLevel);
		printXmlElement("phone", contact.getPhone(), writer, indentLevel);
		this.indent(writer, indentLevel);
		writer.println("</contact>");
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
			writer.println('>');
		}
	}

	private void indent(PrintWriter writer, int level) {
		for (int i = 0; i < level; i++) {
			writer.write('\t');
		}
	}
}
