package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.DisabledElementException;
import pantheist.testhelpers.ui.except.IncorrectTextException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * This class represents a CSS Path. It offers all of the possible interfaces,
 * not all of which may necessarily be relevant.
 */
public class CssPath implements ClickableText, ContainerElement, TextEntry
{
	private static final Logger LOGGER = LogManager.getLogger(CssPath.class);
	private final UiSession session;
	private final String path;
	private final Tweaks tweaks;

	private CssPath(final UiSession session, final String path, final Tweaks tweaks)
	{
		this.session = checkNotNull(session);
		this.path = checkNotNull(path);
		this.tweaks = checkNotNull(tweaks);
	}

	public static ContainerElement root(final WebDriver webDriver)
	{
		return new CssPath(UiSessionImpl.from(webDriver), "", Tweaks.DEFAULT);
	}

	static CssPath of(final UiSession session, final String path, final Tweaks tweaks)
	{
		return new CssPath(session, path, tweaks);
	}

	@Override
	public boolean isVisible()
	{
		final List<WebElement> results = session.find(path);
		if (results.size() > 1)
		{
			throw new MultipleElementException("Multiple elements match css selector: " + path);
		}
		else if (results.isEmpty() || !results.get(0).isDisplayed())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @return the corresponding selenium web element if it's visible
	 * @throws MultipleElementException
	 * @throws CannotFindElementException
	 */
	private WebElement visibleElement()
	{
		final List<WebElement> results = session.find(path);
		if (results.size() > 1)
		{
			throw new MultipleElementException("Multiple elements match css selector: " + path);
		}
		else if (results.isEmpty())
		{
			throw new CannotFindElementException("No element matches css selector: " + path);
		}
		else if (!results.get(0).isDisplayed())
		{
			throw new CannotFindElementException("Element exists but is not displayed: " + path);
		}
		else
		{
			return results.get(0);
		}
	}

	/**
	 * Includes retries.
	 *
	 * @return the corresponding selenium web element if it's visible and
	 *         enabled.
	 */
	private WebElement enabledElement()
	{
		return session.retry(() -> {
			final WebElement result = visibleElement();
			if (!result.isEnabled())
			{
				throw new DisabledElementException("Element is visible but disabled: " + path);
			}
			return result;
		});
	}

	@Override
	public void assertVisible()
	{
		session.retry(this::visibleElement);
	}

	@Override
	public void click()
	{
		enabledElement().click();
		session.disrupt();
	}

	@Override
	public String text()
	{
		final WebElement el = visibleElement();
		final String result;
		if (tweaks.textIsValueAttribute())
		{
			result = el.getAttribute("value");
		}
		else
		{
			result = el.getText();
		}
		checkNotNull(result);
		return result;
	}

	private ExtendedElementFinder finder(final String elementType)
	{
		return ElementFinderImpl.elementType(session, path, elementType);
	}

	@Override
	public ElementFinder<? extends ClickableText> a()
	{
		return finder("a");
	}

	@Override
	public ElementFinder<? extends ContainerElement> div()
	{
		return finder("div");
	}

	@Override
	public void dump(final String... attributes)
	{
		final List<WebElement> elements = session.find(path);
		LOGGER.info("DUMP {}", path);
		LOGGER.info("Matches {} elements", elements.size());
		for (final WebElement el : elements)
		{
			LOGGER.info("=== {} ===", el.getTagName());
			LOGGER.info("Displayed: {}", el.isDisplayed());
			LOGGER.info("Text: {}", el.getText());
			for (final String attr : attributes)
			{
				LOGGER.info("{}: {}", attr, el.getAttribute(attr));
			}
		}
	}

	@Override
	public ElementFinder<? extends TextEntry> inputText()
	{
		return finder("input").withAttrib("type", "text").tweak(Tweaks.INPUT_TEXT);
	}

	@Override
	public void fillOut(final String text)
	{
		final String oldText = text();
		if (!oldText.isEmpty())
		{
			throw new IncorrectTextException("Text not initially empty for " + path + ", instead " + oldText);
		}
		enabledElement().sendKeys(text);
		checkText(text);
	}

	@Override
	public ElementFinder<? extends ClickableText> inputButton()
	{
		return finder("input").withAttrib("type", "button").tweak(Tweaks.INPUT_BUTTON);
	}

	private Void checkText(final String expectedText)
	{
		final String actualText = text();
		if (!actualText.equals(expectedText))
		{
			throw new IncorrectTextException(
					"Text incorrect for " + path + ", should have been " + expectedText + ", instead " + actualText);
		}
		return null;
	}

	@Override
	public void assertText(final String expectedText)
	{
		checkNotNullOrEmpty(expectedText);
		session.retry(() -> checkText(expectedText));
	}
}
