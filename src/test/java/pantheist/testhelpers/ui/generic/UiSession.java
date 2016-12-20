package pantheist.testhelpers.ui.generic;

import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.WebElement;

/**
 * Represents a UI session.
 *
 * This encapsulates a WebDriver, providing WebElement interfaces as needed.
 *
 * It also stores the last time the user did something disruptive, such as
 * clicking on something. This can be used to retry operations and give up only
 * when we are sure the UI has settled.
 */
interface UiSession
{
	/**
	 * Return a list of selenium WebElement from the given css selector.
	 *
	 * @param cssSelector
	 * @return
	 */
	List<WebElement> find(String cssSelector);

	/**
	 * Sets the disruption timestamp.
	 */
	void disrupt();

	/**
	 * Retry the given fn if it throws an exception. Give up when the last call
	 * to disrupt() was too long ago.
	 *
	 * @param fn
	 * @return
	 */
	<T> T retry(Supplier<T> fn);
}
