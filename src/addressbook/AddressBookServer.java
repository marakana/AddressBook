package addressbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AddressBookServer {
	private static final String USAGE = "Main <port> <address-book-factory-type> <prop-name>=<prop-value> ...";

	private static final String PROMPT = "address-book> ";
	private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

	private static void error(PrintStream out, String error, boolean usage) {
		out.println("ERROR: " + error);
		if (usage) {
			out.println(HELP);
		}
	}

	public static void main(String[] args) throws IOException,
			AddressBookException {
		if (args.length < 2) {
			System.err.println(USAGE);
			return;
		}
		int port = Integer.parseInt(args[0]);
		AddressBookFactory.Builder builder = new AddressBookFactory.Builder(
				args[1]);
		for (int i = 2; i < args.length; i++) {
			try {
				builder.setProperty(args[i]);
			} catch (IllegalArgumentException e) {
				System.err.println("ERROR: " + e.getMessage());
				System.err.println(USAGE);
				return;
			}
		}
		AddressBook addressBook = builder.getAddressBookFactory()
				.getAddressBook();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Listening on port " + port);
		try {
			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Handling connection from "
							+ clientSocket.getRemoteSocketAddress());
					try {
						handle(addressBook, clientSocket.getInputStream(),
								clientSocket.getOutputStream());
					} finally {
						clientSocket.close();
					}
				} catch (IOException e) {
					System.err.println("Error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} finally {
			System.out.println("Closing server socket");
			serverSocket.close();
		}
	}

	private static void handle(AddressBook addressBook, InputStream in,
			OutputStream actualOut) throws IOException, AddressBookException {
		PrintStream out = new PrintStream(actualOut);
		out.print(PROMPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			if ("quit".equals(line)) {
				break;
			} else if ("help".equals(line)) {
				out.println("OK");
				out.println(HELP);
			} else if ("list".equals(line)) {
				for (Contact contact : addressBook.getAll()) {
					out.println(contact);
				}
				out.println("OK");
			} else {
				String[] request = line.split(" ");
				if (request.length >= 2) {
					if ("get".equals(request[0])) {
						Contact contact = addressBook.getByEmail(request[1]);
						if (contact != null) {
							out.println(contact);
						}
						out.println("OK");
					} else if ("delete".equals(request[0])) {
						addressBook.deleteByEmail(request[1]);
						out.println("OK");
					} else if ("store".equals(request[0])
							&& request.length >= 4) {
						Contact contact = new Contact(request[1], request[2],
								request[3]);
						if (request.length >= 5) {
							contact.setPhone(request[4]);
						}
						addressBook.store(contact);
						out.println("OK");
					} else {
						error(out, "Invalid request", true);
					}
				} else {
					error(out, "Invalid request", true);
				}
			}
			if (out.checkError()) {
				break;
			}
			out.print(PROMPT);
			out.flush();
		}
	}
}
