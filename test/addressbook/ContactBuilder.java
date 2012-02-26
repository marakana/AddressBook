package addressbook;

public class ContactBuilder {
	private Contact contact;

	public static ContactBuilder build(String email) {
		ContactBuilder contactBuilder = new ContactBuilder();
		contactBuilder.contact = new Contact(email);
		return contactBuilder;
	}

	public ContactBuilder withFirstName(String firstName) {
		this.contact.setFirstName(firstName);
		return this;
	}

	public ContactBuilder withLastName(String lastName) {
		this.contact.setLastName(lastName);
		return this;
	}

	public ContactBuilder withPhone(String phone) {
		this.contact.setPhone(phone);
		return this;
	}

	public Contact getContact() {
		return this.contact;
	}

}
