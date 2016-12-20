package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Throwables;

import pantheist.testhelpers.ui.except.UiStateException;

public final class UiSessionImpl implements UiSession
{
	private static final Logger LOGGER = LogManager.getLogger(UiSessionImpl.class);
	private static final long DISRUPTION_TIME = 500;
	private static final long POLLING_TIME = 100;
	private final WebDriver webDriver;

	// State
	long disruptionTimestamp;

	private UiSessionImpl(final WebDriver webDriver)
	{
		this.webDriver = checkNotNull(webDriver);
		this.disruptionTimestamp = System.currentTimeMillis();
	}

	public static UiSession from(final WebDriver webDriver)
	{
		return new UiSessionImpl(webDriver);
	}

	@Override
	public List<WebElement> find(final String cssSelector)
	{
		return webDriver.findElements(By.cssSelector(cssSelector));
	}

	@Override
	public void disrupt()
	{
		disruptionTimestamp = System.currentTimeMillis();
	}

	@Override
	public <T> T retry(final Supplier<T> fn)
	{
		while (true)
		{
			try
			{
				return fn.get();
			}
			catch (final UiStateException e)
			{
				LOGGER.trace(e);
				if (System.currentTimeMillis() - disruptionTimestamp > DISRUPTION_TIME)
				{
					// Last disruption was too long ago. Give up by rethrowing the exception.
					throw e;
				}
				sleep();
			}
		}
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(POLLING_TIME);
		}
		catch (final InterruptedException e)
		{
			Throwables.propagate(e);
		}
	}

}
