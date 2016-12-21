package pantheist.testhelpers.selenium;

public class Interaction
{
	private Interaction()
	{
		throw new UnsupportedOperationException();
	}

	public static ApiRule api()
	{
		return new NoSelenium();
	}

	public static ApiRule hidden()
	{
		return new FirefoxRule(false);
	}

	public static ApiRule visible()
	{
		return new FirefoxRule(true);
	}
}
