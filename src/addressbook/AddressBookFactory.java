package addressbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory for building address book. Must have a public no-arg constructor.
 * Properties for the factory will be passed via the setter methods that take a
 * single string parameter.
 */
public interface AddressBookFactory {
    public AddressBook getAddressBook() throws AddressBookException;

    /**
     * A convenience builder for constructing and initializing factories
     */
    public static class Builder {
	private static final Pattern PROP_PATTERN = Pattern
		.compile("([^=]+)=(.+)");
	private final AddressBookFactory addressBookFactory;
	private Map<String, Method> setters = new HashMap<String, Method>();

	public Builder(String type) throws AddressBookException {
	    try {
		Class<?> clazz = Class.forName(type);
		this.addressBookFactory = (AddressBookFactory) clazz
			.newInstance();
		for (Method method : clazz.getDeclaredMethods()) {
		    String name = method.getName();
		    if (name.startsWith("set")
			    && method.getParameterTypes().length == 1
			    && method.getParameterTypes()[0] == String.class
			    && method.getReturnType() == void.class
			    && Modifier.isPublic(method.getModifiers())
			    && !Modifier.isStatic(method.getModifiers())) {
			name = name.substring(3);
			name = Character.toLowerCase(name.charAt(0))
				+ name.substring(1);
			this.setters.put(name, method);
		    }
		}
	    } catch (Exception e) {
		throw new AddressBookException(
			"Failed to get address book factory for type [" + type
				+ "]", e);
	    }
	}

	public void setProperty(String name, String value)
		throws AddressBookException {
	    Method method = this.setters.remove(name);
	    if (method == null) {
		throw new AddressBookException("No such property [" + name
			+ "] on ["
			+ this.addressBookFactory.getClass().getName() + "]");
	    }
	    try {
		method.invoke(this.addressBookFactory, value);
	    } catch (Exception e) {
		throw new AddressBookException("Failed to set property ["
			+ name + "]=[" + value + "] on ["
			+ this.addressBookFactory.getClass().getName() + "]", e);
	    }
	}

	public void setProperty(String property) throws AddressBookException {
	    Matcher m = PROP_PATTERN.matcher(property);
	    if (m.matches()) {
		this.setProperty(m.group(1), m.group(2));
	    } else {
		throw new IllegalArgumentException("Invalid property ["
			+ property
			+ "]. Expecting property in <name>=<value> format.");
	    }
	}

	public AddressBookFactory getAddressBookFactory()
		throws AddressBookException {
	    // go through all remaining setters
	    for (Map.Entry<String, Method> entry : this.setters.entrySet()) {
		String name = entry.getKey();
		Method method = entry.getValue();
		Annotation annotation = null;
		// if the setter has a default value
		if ((annotation = Util.getByType(Default.class,
			method.getAnnotations())) != null) {
		    // get the value
		    String value = ((Default) annotation).value();
		    try {
			// set the default value
			method.invoke(this.addressBookFactory, value);
		    } catch (Exception e) {
			// handle error
			throw new AddressBookException(
				"Failed to set default property ["
					+ name
					+ "]=["
					+ value
					+ "] on ["
					+ this.addressBookFactory.getClass()
						.getName() + "]", e);
		    }

		} else if (Util.getByType(Required.class,
			method.getAnnotations()) != null) {
		    // the setter is required
		    throw new AddressBookException("Required property [" + name
			    + "] has not been set on ["
			    + this.addressBookFactory.getClass().getName()
			    + "]");
		}
	    }
	    return this.addressBookFactory;
	}
    }
}
