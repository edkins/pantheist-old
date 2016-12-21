package pantheist.testhelpers.ui.generic;

/**
 * Represents an element which can contain other elements.
 *
 * Finding child elements generally consists of three steps:
 *
 * - identifying the type of element you're looking for (which also determines
 * which interface gets exposed)
 *
 * - some additional information to find the element, e.g. its id or css class.
 *
 * - a final .choose()
 *
 * The process looks like:
 *
 * containerElement.a().withId("my-link").choose()
 *
 * although note that you can chain additional steps in there as well.
 *
 * The ElementFinder interface assists with this.
 */
public interface ContainerElement extends VisualElement
{
	/**
	 * Find an html 'a' element.
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends ClickableText> a();

	/**
	 * Find an html 'div' element.
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends ContainerElement> div();

	/**
	 * Find an html 'input' element with type="text".
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends TextEntry> inputText();

	/**
	 * Find an html 'input' element with type="button".
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends ClickableText> inputButton();

	/**
	 * Find an html 'select' element
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends Menu> select();

	/**
	 * Find an html 'table' element
	 * 
	 * @return element finder
	 */
	ElementFinder<? extends ProtoTable> table();

}
