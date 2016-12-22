package pantheist.testhelpers.session;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

import pantheist.testhelpers.selenium.SeleniumInfo;
import pantheist.testhelpers.ui.pan.PantheistUi;

public interface TestSession extends SeleniumInfo
{
	void clear();

	void supplyDataDir(File dataDir);

	WebDriver webDriver();

	PantheistUi ui();

	int pantheistPort();

	URL pantheistUrl();

	File dataDir();

	File dumpFile(String prefix, String ext);

	ObjectMapper objectMapper();
}
