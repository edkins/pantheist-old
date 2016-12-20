package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Represents an element which would normally be shown on the page. You can
 * query it as to whether it's really visible. This does not distinguish between
 * elements that don't exist at all (because the css path matches nothing) and
 * those that are simply made invisible, since the test shouldn't usually care.
 */
public interface VisualElement extends AnyElement
{
	/**
	 * @return whether the elemenet is present and visible.
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 */
	boolean isVisible();

	/**
	 * Fails if the element does not exist or is invisible.
	 *
	 * @throws CannotFindElementException
	 *             if the element does not exist or is invisible
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 */
	void assertVisible();
}
