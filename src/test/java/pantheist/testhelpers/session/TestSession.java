package pantheist.testhelpers.session;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.WebDriver;

import pantheist.testhelpers.ui.pan.PantheistUi;

public interface TestSession
{
	void clear();

	void supplyWebDriver(WebDriver webDriver);

	void supplyDataDir(File dataDir);

	WebDriver webDriver();

	PantheistUi ui();

	int pantheistPort();

	URL pantheistUrl();

	File dataDir();
}
