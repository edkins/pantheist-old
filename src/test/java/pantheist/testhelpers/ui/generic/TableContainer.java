package pantheist.testhelpers.ui.generic;

/**
 * Internal interface for dealing with tables. Similar to ContainerElement.
 */
interface TableContainer<T> extends ContainerElement
{
	/**
	 * Useful for:
	 *
	 * - finding tr within a thead/tbody
	 *
	 * - finding td/th within a tr
	 *
	 * @return finder for elements that are direct children of this element
	 */
	T child(int index);

	int childCount();
}
