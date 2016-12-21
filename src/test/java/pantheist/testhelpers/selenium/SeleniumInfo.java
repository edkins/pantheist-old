package pantheist.testhelpers.selenium;

import org.openqa.selenium.WebDriver;

import pantheist.testhelpers.session.TestMode;

public interface SeleniumInfo
{
	/**
	 * @return web driver for this session
	 * @throws UnsupportedOperationException
	 *             if selenium is not enabled.
	 */
	WebDriver webDriver();

	TestMode mode();
}
