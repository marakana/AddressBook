package addressbook;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContactTest {

	@Test
	public void constructor() {
		Contact c = new Contact("John", "Smith", "john.smith@email.com");
		assertEquals("John", c.getFirstName());
		assertEquals("Smith", c.getLastName());
		assertEquals("john.smith@email.com", c.getEmail());
	}

	@Test(expected = NullPointerException.class)
	public void constructorWithNullEmail() {
		new Contact("John", "Smith", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWithEmptyEmail() {
		new Contact("John", "Smith", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWithInvalidEmail() {
		new Contact("John", "Smith", "invalid email address");
	}

	@Test
	public void equalsSameProperties() {
		Contact c1 = new Contact("John", "Smith", "john.smith@email.com");
		Contact c2 = new Contact("John", "Smith", "john.smith@email.com");
		assertEquals(c1, c2);
	}

	@Test
	public void equalsWithNullProperties() {
		Contact c1 = new Contact(null, null, "john.smith@email.com");
		Contact c2 = new Contact(null, null, "john.smith@email.com");
		assertEquals(c1, c2);
	}

	@Test
	public void equalsSameEmail() {
		Contact c1 = new Contact("John1", "Smith1", "john.smith@email.com");
		Contact c2 = new Contact("John2", "Smith2", "john.smith@email.com");
		assertEquals(c1, c2);
	}

	@Test
	public void equalsDifferentEmail() {
		Contact c1 = new Contact("John", "Smith", "john.smith1@email.com");
		Contact c2 = new Contact("John", "Smith", "john.smith2@email.com");
		assertFalse(c1.equals(c2));
	}

	@Test
	public void hashCodeSameEmail() {
		Contact c1 = new Contact("John", "Smith", "john.smith@email.com");
		Contact c2 = new Contact("John", "Smith", "john.smith@email.com");
		assertEquals(c1.hashCode(), c2.hashCode());
	}

	@Test
	public void hashCodeDifferentEmail() {
		Contact c1 = new Contact("John", "Smith", "john.smith1@email.com");
		Contact c2 = new Contact("John", "Smith", "john.smith2@email.com");
		assertFalse(c1.hashCode() == c2.hashCode());
	}
}
