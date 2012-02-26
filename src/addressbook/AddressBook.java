package addressbook;

import java.util.List;

public interface AddressBook {

	/**
	 * Get contact by email.
	 * 
	 * @param email
	 *            the email of the contact to get
	 * @return the contact with the specified email or null if no such contact
	 *         exists.
	 * @throws DataAccessException
	 *             if there is a problem getting this contact.
	 * @throws NullPointerException
	 *             if email is null
	 */
	public Contact getByEmail(String email) throws DataAccessException;

	/**
	 * Get all contacts
	 * 
	 * @return all contacts sorted by first then last names, never null.
	 * @throws DataAccessException
	 *             if there is a problem getting contacts
	 */
	public List<Contact> getAll() throws DataAccessException;

	/**
	 * Store a contact
	 * 
	 * @param contact
	 *            the contact to store
	 * @throws DataAccessException
	 *             if there is a problem storing this contact
	 * @throws NullPointerException
	 *             if contact is null
	 */
	public void store(Contact contact) throws DataAccessException;

	/**
	 * Delete contact by email. If no such contact exists, this method does
	 * nothing.
	 * 
	 * @param email
	 *            the email of the contact to delete
	 * @throws DataAccessException
	 *             if there is a problem deleting this contact.
	 * @throws NullPointerException
	 *             if email is null
	 */
	public void deleteByEmail(String email) throws DataAccessException;

	public void close() throws DataAccessException;
}
