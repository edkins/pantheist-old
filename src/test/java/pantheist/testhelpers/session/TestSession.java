package pantheist.testhelpers.session;

import java.net.URL;

import org.openqa.selenium.WebDriver;

import pantheist.testhelpers.ui.pan.PantheistUi;

public interface TestSession
{
	void clear();

	void supplyWebDriver(WebDriver webDriver);

	WebDriver webDriver();

	PantheistUi ui();

	int pantheistPort();

	URL pantheistUrl();
}
