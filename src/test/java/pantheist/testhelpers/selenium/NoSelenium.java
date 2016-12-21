package pantheist.testhelpers.selenium;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;

import pantheist.testhelpers.session.TestMode;

final class NoSelenium implements ApiRule
{
	NoSelenium()
	{

	}

	@Override
	public WebDriver webDriver()
	{
		throw new UnsupportedOperationException("No selenium for this test");
	}

	@Override
	public TestMode mode()
	{
		return TestMode.API;
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return base;
	}
}
