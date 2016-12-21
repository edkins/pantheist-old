package pantheist.testhelpers.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import pantheist.common.util.MutableOptional;
import pantheist.testhelpers.selenium.SeleniumInfo;
import pantheist.testhelpers.ui.pan.PantheistUi;
import pantheist.testhelpers.ui.pan.PantheistUiImpl;

public final class TestSessionImpl implements TestSession
{
	private final PortFinder pantheistPort;

	private final SeleniumInfo seleniumInfo;

	private final MutableOptional<File> dataDir;

	private final ObjectMapper objectMapper;

	private TestSessionImpl(final SeleniumInfo seleniumInfo)
	{
		this.pantheistPort = PortFinder.empty();
		this.seleniumInfo = checkNotNull(seleniumInfo);
		this.dataDir = MutableOptional.empty();
		this.objectMapper = new ObjectMapper();
	}

	public static TestSession forNewTest(final SeleniumInfo seleniumInfo)
	{
		return new TestSessionImpl(seleniumInfo);
	}

	@Override
	public void clear()
	{
		pantheistPort.clear();
		dataDir.clear();
	}

	@Override
	public WebDriver webDriver()
	{
		return seleniumInfo.webDriver();
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
		return PantheistUiImpl.root(webDriver(), objectMapper);
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

	@Override
	public ObjectMapper objectMapper()
	{
		return objectMapper;
	}

	@Override
	public TestMode mode()
	{
		return seleniumInfo.mode();
	}

}
