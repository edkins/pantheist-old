package pantheist.testhelpers.selenium;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverCommandExecutor;

import com.google.common.collect.ImmutableMap;

import pantheist.testhelpers.session.TestSession;

public class FirefoxRule implements TestRule
{
	private static final int XVFB_DISPLAY = 99;

	private final TestSession session;

	private FirefoxRule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	public static TestRule forTest(final TestSession session)
	{
		return new FirefoxRule(session);
	}

	private static class FirefoxWithEnvDriver extends RemoteWebDriver
	{
		public FirefoxWithEnvDriver()
		{
			super(createCommandExecutor(), new DesiredCapabilities(), new DesiredCapabilities());
		}

		private static final CommandExecutor createCommandExecutor()
		{
			final GeckoDriverService.Builder builder = new GeckoDriverService.Builder();
			builder.usingPort(0);
			builder.withEnvironment(ImmutableMap.of("DISPLAY", ":" + XVFB_DISPLAY));
			builder.usingDriverExecutable(geckoDriver());
			return new DriverCommandExecutor(builder.build());
		}

		private static File geckoDriver()
		{
			return new File(System.getProperty("user.home") + "/bin/geckodriver");
		}
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement() {

			@Override
			public void evaluate() throws Throwable
			{
				final WebDriver webDriver = new FirefoxWithEnvDriver();
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
