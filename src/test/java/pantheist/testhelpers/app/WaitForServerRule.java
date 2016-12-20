package pantheist.testhelpers.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import pantheist.testhelpers.selenium.NavigateToHomeRule;
import pantheist.testhelpers.session.TestSession;

public class WaitForServerRule implements TestRule
{
	private static final Logger LOGGER = LogManager.getLogger(NavigateToHomeRule.class);
	private final TestSession session;

	private WaitForServerRule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	public static TestRule forTest(final TestSession session)
	{
		return new WaitForServerRule(session);
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement() {

			@Override
			public void evaluate() throws Throwable
			{
				pollServer();
				base.evaluate();
			}
		};
	}

	private void pollServer() throws InterruptedException, ServerNeverAppearedException
	{
		for (int i = 0; i < 20; i++)
		{
			try
			{
				session.pantheistUrl().getContent();
				return;
			}
			catch (final IOException e)
			{
				LOGGER.trace(e);
				Thread.sleep(100);
			}
		}
		throw new ServerNeverAppearedException();
	}

}
