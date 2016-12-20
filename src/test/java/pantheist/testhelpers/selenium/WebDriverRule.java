package pantheist.testhelpers.selenium;

import static com.google.common.base.Preconditions.checkNotNull;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.firefox.FirefoxDriver;

import pantheist.testhelpers.session.TestSession;

public class WebDriverRule implements TestRule
{
	private final TestSession session;

	private WebDriverRule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	public static TestRule forTest(final TestSession session)
	{
		return new WebDriverRule(session);
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement() {

			@Override
			public void evaluate() throws Throwable
			{
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/bin/geckodriver");
				final FirefoxDriver webDriver = new FirefoxDriver();
				try
				{
					session.supplyWebDriver(webDriver);
					base.evaluate();
				}
				finally
				{
					webDriver.quit();
				}
			}
		};
	}

}
