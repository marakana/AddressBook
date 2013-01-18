package addressbook;

import static addressbook.Util.compare;

import java.io.Serializable;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact implements Serializable, Comparable<Contact> {

    public static final Comparator<Contact> LAST_NAME_FIRST_NAME_COMPARATOR = new Comparator<Contact>() {

	@Override
	public int compare(Contact o1, Contact o2) {
	    int result = Util.compare(o1.getLastName(), o2.getLastName());
	    return result == 0 ? Util.compare(o1.getFirstName(),
		    o2.getFirstName()) : result;
	}

    };

    private static final Pattern PARSE_PATTERN = Pattern
	    .compile("([^ ]+) ([^ ]+) <([^ ]+)> (?:\\(([^ ]+)\\))?");

    private static final long serialVersionUID = 3293493664857316093L;
    private String firstName;
    private String lastName;
    private final String email;
    private String phone;

    public static Contact parse(String in) {
	Matcher m = PARSE_PATTERN.matcher(in);
	if (m.matches()) {
	    return new Contact(m.group(1), m.group(2), m.group(3), m.group(4));
	} else {
	    throw new IllegalArgumentException("Invalid input string: " + in);
	}
    }

    /**
     * 
     * @param email
     * @throws NullPointerException
     *             if email is null
     * @throws IllegalArgumentException
     *             if email is invalid
     */
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

    public Contact(String firstName, String lastName, String email, String phone) {
	this(firstName, lastName, email);
	this.phone = phone;
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

    public Contact withPhone(String phone) {
	this.phone = phone;
	return this;
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
	    Contact that = (Contact) obj;
	    return this.getEmail().equals(that.getEmail());
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
	return String.format("%s %s <%s> (%s)", this.getFirstName(),
		this.getLastName(), this.getEmail(), this.getPhone());
    }

    @Override
    public int compareTo(Contact other) {
	int result = compare(this.getFirstName(), other.getFirstName());
	return result == 0 ? compare(this.getLastName(), other.getLastName())
		: result;
    }

}
