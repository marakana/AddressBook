package addressbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	private static final String PROMPT = "address-book> ";
	private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

	private static void error(String error, boolean usage) {
		System.err.println("ERROR: " + error);
		if (usage) {
			System.err.println(HELP);
		}
	}

	public static void main(String[] args) throws IOException,
			DataAccessException {
		AddressBook addressBook = new SerializingFileBaseAddressBook(new File(
				args[0]));
		System.out.print(PROMPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line;
		while ((line = reader.readLine()) != null) {
			if ("quit".equals(line)) {
				break;
			} else if ("help".equals(line)) {
				System.out.println("OK");
				System.out.println(HELP);
			} else if ("list".equals(line)) {
				System.out.println("OK");
				for (Contact contact : addressBook.getAll()) {
					System.out.println(contact);
				}
			} else {
				String[] request = line.split(" ");
				if (request.length >= 2) {
					if ("get".equals(request[0])) {
						Contact contact = addressBook.getByEmail(request[1]);
						if (contact == null) {
							error("No such contact", false);
						} else {
							System.out.println("OK");
							System.out.println(contact);
						}
					} else if ("delete".equals(request[0])) {
						addressBook.deleteByEmail(request[1]);
						System.out.println("OK");
					} else if ("store".equals(request[0])
							&& request.length >= 4) {
						Contact contact = new Contact(request[1], request[2],
								request[3]);
						if (request.length >= 5) {
							contact.setPhone(request[4]);
						}
						addressBook.store(contact);
						System.out.println("OK");
					} else {
						error("Invalid request", true);
					}
				} else {
					error("Invalid request", true);
				}
			}
			System.out.print(PROMPT);
		}
	}
}
