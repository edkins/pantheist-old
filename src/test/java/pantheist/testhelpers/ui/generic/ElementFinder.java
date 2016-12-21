package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Used by ContainerElement to find elements of a particular type. This
 * interface offers the next step: to find a particular element by id or css
 * class.
 *
 * Note that you can ask for any old garbage and something will get returned to
 * you here. It's only when you call .assertVisible() or one of the other
 * accessors that it will assert the element's existence.
 *
 * @param <T>
 *            The interface that will get returned.
 */
public interface ElementFinder<T>
{
	/**
	 * Find an element by its id.
	 *
	 * @param id
	 *            CSS id of the element to search for
	 * @return interface to access this element
	 * @throws IllegalArgumentException
	 *             if id is empty or contains problematic characters
	 */
	T withId(String id);

	/**
	 * Find an element by its css class.
	 *
	 * @param cssClass
	 *            CSS class of the element to search for
	 * @return interface to access this element
	 * @throws IllegalArgumentException
	 *             if cssClass is empty or contains problematic characters
	 */
	T withClass(String cssClass);

	/**
	 * Finds an element by its value attribute. Most useful for form elements
	 * such as input buttons.
	 *
	 * @param value
	 * @return
	 */
	T withValue(String value);

	/**
	 * If there's only one element in this selection, return it.
	 *
	 * If there are multiple elements then you'll get
	 * {@link MultipleElementException} when you try to access them.
	 *
	 * @return interface to access this element
	 */
	T choose();

	/**
	 * Find an element by html data attribute.
	 *
	 * @param key
	 *            data key name without "data-" prepended
	 * @param value
	 *            data value (may be empty but not null)
	 * @return finder to allow you to chain these together
	 * @throws IllegalArgumentException
	 *             if key or value contains problematic characters, or key is
	 *             empty
	 */
	ElementFinder<T> withData(String key, String value);

	/**
	 * @return corresponding element collection
	 */
	ElementCollection all();
}
