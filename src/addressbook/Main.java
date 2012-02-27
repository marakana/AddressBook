package addressbook;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	private static final String USAGE = "Main <address-book-factory-type> <prop-name>=<prop-value> ...";
	private static final String PROMPT = "address-book> ";
	private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

	private static void error(String error, boolean usage) {
		System.err.println("ERROR: " + error);
		if (usage) {
			System.err.println(HELP);
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println(USAGE);
			return;
		}
		AddressBookFactory.Builder builder = new AddressBookFactory.Builder(
				args[0]);
		for (int i = 1; i < args.length; i++) {
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

		System.out.print(PROMPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line;
		while ((line = reader.readLine()) != null) {
			if ("quit".equals(line)) {
				addressBook.close();
				break;
			} else if ("help".equals(line)) {
				System.out.println("OK");
				System.out.println(HELP);
			} else if ("list".equals(line)) {
				for (Contact contact : addressBook.getAll()) {
					System.out.println(contact);
				}
				System.out.println("OK");
			} else {
				String[] request = line.split(" ");
				if (request.length >= 2) {
					if ("get".equals(request[0])) {
						Contact contact = addressBook.getByEmail(request[1]);
						if (contact == null) {
							error("No such contact", false);
						} else {
							System.out.println(contact);
							System.out.println("OK");
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
