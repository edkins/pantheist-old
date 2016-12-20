package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.DisabledElementException;
import pantheist.testhelpers.ui.except.EmptyTextException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * This class represents a CSS Path. It offers all of the possible interfaces,
 * not all of which may necessarily be relevant.
 */
public class CssPath implements ClickableText, ContainerElement
{
	private static final Logger LOGGER = LogManager.getLogger(CssPath.class);
	private final WebDriver webDriver;
	private final String path;

	private CssPath(final WebDriver webDriver, final String path)
	{
		this.webDriver = checkNotNull(webDriver);
		this.path = checkNotNull(path);
	}

	public static ContainerElement root(final WebDriver webDriver)
	{
		return new CssPath(webDriver, "");
	}

	static CssPath of(final WebDriver webDriver, final String path)
	{
		return new CssPath(webDriver, path);
	}

	@Override
	public boolean isVisible()
	{
		final List<WebElement> results = webDriver.findElements(By.cssSelector(path));
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
		final List<WebElement> results = webDriver.findElements(By.cssSelector(path));
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
	 * @return the corresponding selenium web element if it's visible and
	 *         enabled.
	 */
	private WebElement enabledElement()
	{
		final WebElement result = visibleElement();
		if (!result.isEnabled())
		{
			throw new DisabledElementException("Element is visible but disabled: " + path);
		}
		return result;
	}

	@Override
	public void assertVisible()
	{
		visibleElement();
	}

	@Override
	public void click()
	{
		enabledElement().click();
	}

	@Override
	public String text()
	{
		final String result = visibleElement().getText();
		if (result == null || result.isEmpty())
		{
			throw new EmptyTextException("No text for element: " + path);
		}
		return result;
	}

	private ElementFinder<CssPath> finder(final String elementType)
	{
		return ElementFinderImpl.elementType(webDriver, path, elementType);
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
		final List<WebElement> elements = webDriver.findElements(By.cssSelector(path));
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

}
