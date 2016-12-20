package pantheist.testhelpers.session;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;

import com.google.common.base.Throwables;

import pantheist.common.util.MutableOptional;
import pantheist.testhelpers.ui.pan.PantheistUi;
import pantheist.testhelpers.ui.pan.PantheistUiImpl;

public final class TestSessionImpl implements TestSession
{
	private final PortFinder pantheistPort;

	private final MutableOptional<WebDriver> webDriver;

	private final MutableOptional<File> dataDir;

	private TestSessionImpl()
	{
		pantheistPort = PortFinder.empty();
		webDriver = MutableOptional.empty();
		dataDir = MutableOptional.empty();
	}

	public static TestSession forNewTest()
	{
		return new TestSessionImpl();
	}

	@Override
	public void clear()
	{
		pantheistPort.clear();
		webDriver.clear();
	}

	@Override
	public void supplyWebDriver(final WebDriver newWebDriver)
	{
		webDriver.add(newWebDriver);
	}

	@Override
	public WebDriver webDriver()
	{
		return webDriver.get();
	}

	@Override
	public int pantheistPort()
	{
		return pantheistPort.get();
	}

	@Override
	public URL pantheistUrl()
	{
		try
		{
			return new URL("http://localhost:" + pantheistPort.get());
		}
		catch (final MalformedURLException e)
		{
			throw Throwables.propagate(e);
		}
	}

	@Override
	public PantheistUi ui()
	{
		return PantheistUiImpl.root(webDriver());
	}

	@Override
	public void supplyDataDir(final File newDataDir)
	{
		dataDir.add(newDataDir);
	}

	@Override
	public File dataDir()
	{
		return dataDir.get();
	}

}
