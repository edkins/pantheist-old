package pantheist.testhelpers.ui.generic;

/**
 * Represents an element which can contain other elements.
 *
 * Finding child elements generally consists of two steps:
 *
 * - identifying the type of element you're looking for (which also determines
 * which interface gets exposed)
 *
 * - some additional information to find the element, e.g. its id or css class.
 *
 * The process looks like:
 *
 * containerElement.a().withId("my-link")
 *
 * although note that you can chain additional steps in there as well, such as
 * .withData
 *
 * If there's only one element of that type then you can access it with .choose
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
	 * Find an html 'p' element.
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends ContainerWithText> p();

	/**
	 * Find an html 'input' element with type="text".
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends TextEntry> inputText();

	/**
	 * Find an html 'textarea' element.
	 *
	 * @return element finder.
	 */
	ElementFinder<? extends TextEntry> textarea();

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
