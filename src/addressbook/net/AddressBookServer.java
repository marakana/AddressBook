package addressbook.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import addressbook.AddressBook;
import addressbook.AddressBookException;
import addressbook.AddressBookFactory;
import addressbook.Contact;

public class AddressBookServer {
    private static final String USAGE = "Main <port> <max-clients> <address-book-factory-type> <prop-name>=<prop-value> ...";

    private static final String PROMPT = "address-book> ";
    private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

    public static void main(String[] args) throws IOException,
	    AddressBookException {
	if (args.length < 3) {
	    System.err.println(USAGE);
	    return;
	}
	int port = Integer.parseInt(args[0]);
	int maxClients = Integer.parseInt(args[1]);
	AddressBookFactory.Builder builder = new AddressBookFactory.Builder(
		args[2]);
	for (int i = 3; i < args.length; i++) {
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
	ExecutorService executorService = Executors
		.newFixedThreadPool(maxClients);
	System.out.println("Listening on port " + port);
	final AddressBookServer server = new AddressBookServer(addressBook,
		serverSocket, executorService);
	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	    public void run() {
		try {
		    server.shutdown();
		} catch (IOException e) {
		    System.err.println("Failed on shutdown");
		    e.printStackTrace();
		}
	    }
	}));
	server.run();
    }

    private final AddressBook addressBook;
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    public AddressBookServer(AddressBook addressBook,
	    ServerSocket serverSocket, ExecutorService executorService) {
	this.addressBook = addressBook;
	this.serverSocket = serverSocket;
	this.executorService = executorService;
    }

    public void run() {

	while (this.serverSocket.isBound() && !this.serverSocket.isClosed()) {
	    try {
		Socket clientSocket = serverSocket.accept();
		System.out.println("Handling connection from "
			+ clientSocket.getRemoteSocketAddress());
		this.executorService.execute(new Handler(addressBook,
			clientSocket));
	    } catch (SocketException e) {
		if (this.serverSocket.isClosed()) {
		    break;
		} else {
		    System.err.println("Socket error: " + e.getMessage());
		    e.printStackTrace();
		}
	    } catch (IOException e) {
		System.err.println("I/O Error: " + e.getMessage());
		e.printStackTrace();
	    }
	}
	System.out.println("Good-bye");

    }

    public void shutdown() throws IOException {
	System.out.println("Shutting-down");
	this.serverSocket.close();
	List<Runnable> pendingHandlers = this.executorService.shutdownNow();
	for (Runnable pendingHandler : pendingHandlers) {
	    ((Handler) pendingHandler).shutdown();
	}
    }

    private static class Handler implements Runnable {

	private final AddressBook addressBook;
	private final Socket socket;

	public Handler(AddressBook addressBook, Socket socket) {
	    this.addressBook = addressBook;
	    this.socket = socket;
	}

	public void shutdown() {
	    System.out.println("Shutting down socket "
		    + socket.getRemoteSocketAddress());
	    try {
		this.socket.close();
	    } catch (IOException e) {
		System.err.println("Failed to shut down socket");
		e.printStackTrace();
	    }
	}

	@Override
	public void run() {
	    try {
		try {
		    PrintStream out = new PrintStream(
			    this.socket.getOutputStream());
		    BufferedReader reader = new BufferedReader(
			    new InputStreamReader(this.socket.getInputStream()));
		    out.print(PROMPT);
		    String line;
		    while ((line = reader.readLine()) != null) {
			if ("quit".equals(line)) {
			    break;
			} else if ("help".equals(line)) {
			    out.println("OK");
			    out.println(HELP);
			} else {
			    try {
				if (this.handle(line.split(" "), out)) {
				    out.println("OK");
				} else {
				    error(out, "Invalid request", true);
				}
			    } catch (AddressBookException e) {
				error(out, e.getMessage(), true);
				e.printStackTrace();
			    }
			}
			if (out.checkError()) {
			    break;
			}
			out.print(PROMPT);
			out.flush();
		    }
		} finally {
		    this.socket.close();
		}
	    } catch (IOException e) {
		System.err
			.println("I/O failure while talking to remote client. Aborting.");
		e.printStackTrace();
	    }
	}

	private boolean handle(String[] request, PrintStream out)
		throws AddressBookException {
	    if (request.length >= 1 && "list".equals(request[0])) {
		for (Contact contact : addressBook.getAll()) {
		    out.println(contact);
		}
		return true;
	    } else {
		if (request.length >= 2) {
		    if ("get".equals(request[0])) {
			Contact contact = addressBook.getByEmail(request[1]);
			if (contact != null) {
			    out.println(contact);
			}
			return true;
		    } else if ("delete".equals(request[0])) {
			addressBook.deleteByEmail(request[1]);
			return true;
		    } else if ("store".equals(request[0])
			    && request.length >= 4) {
			Contact contact = new Contact(request[1], request[2],
				request[3]);
			if (request.length >= 5) {
			    contact.setPhone(request[4]);
			}
			addressBook.store(contact);
			return true;
		    }
		}
	    }
	    return false;
	}

	private void error(PrintStream out, String error, boolean usage) {
	    out.println("ERROR: " + error);
	    if (usage) {
		out.println(HELP);
	    }
	}
    }
}
