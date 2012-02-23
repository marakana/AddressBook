package addressbook;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 3293493664857316093L;
	private String firstName;
	private String lastName;
	private final String email;
	private String phone;

	public Contact(String email) {
		if (email == null) {
			throw new NullPointerException("Email must not be null");
		} else if (email.isEmpty()) {
			throw new IllegalArgumentException("Email must not be empty");
		} else if (!email.matches("[^@]+@[^@]+")) {
			throw new IllegalArgumentException("Invalid email address: "
					+ email);
		}
		this.email = email;
	}

	public Contact(String firstName, String lastName, String email) {
		this(email);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Get the email of this contact
	 * 
	 * @return the email of this contact; never null.
	 */
	public String getEmail() {
		return email;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj != null && obj.getClass() == this.getClass()) {
			return this.getEmail().equals(((Contact) obj).getEmail());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 7 + 17 * this.getEmail().hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s %s <%s>", this.getFirstName(),
				this.getLastName(), this.getEmail());
	}
}
