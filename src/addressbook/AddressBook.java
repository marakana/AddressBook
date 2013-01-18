package addressbook;

import java.util.List;

/**
 * A personal address book that can store and retrieve contacts.
 */
public interface AddressBook {

    /**
     * Get contact by email.
     * 
     * @param email
     *            the email of the contact to get
     * @return the contact with the specified email or null if no such contact
     *         exists.
     * @throws AddressBookException
     *             if there is a problem getting this contact.
     * @throws NullPointerException
     *             if email is null
     */
    public Contact getByEmail(String email) throws AddressBookException;

    /**
     * Get all contacts
     * 
     * @return all contacts sorted by first then last names, never null.
     * @throws AddressBookException
     *             if there is a problem getting contacts
     */
    public List<Contact> getAll() throws AddressBookException;

    /**
     * Store a contact
     * 
     * @param contact
     *            the contact to store
     * @throws AddressBookException
     *             if there is a problem storing this contact
     * @throws NullPointerException
     *             if contact is null
     */
    public void store(Contact contact) throws AddressBookException;

    /**
     * Delete contact by email. If no such contact exists, this method does
     * nothing.
     * 
     * @param email
     *            the email of the contact to delete
     * @throws AddressBookException
     *             if there is a problem deleting this contact.
     * @throws NullPointerException
     *             if email is null
     */
    public void deleteByEmail(String email) throws AddressBookException;

    /**
     * Closes this address book, indicating that it is no longer needed.
     * 
     * @throws AddressBookException
     *             if there is a problem closing this address book.
     */
    public void close() throws AddressBookException;
}
