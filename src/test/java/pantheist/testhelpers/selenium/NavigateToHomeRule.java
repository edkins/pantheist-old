package pantheist.testhelpers.selenium;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import pantheist.testhelpers.session.TestSession;

public class NavigateToHomeRule implements TestRule
{
	private static final Logger LOGGER = LogManager.getLogger(NavigateToHomeRule.class);
	private final TestSession session;

	private NavigateToHomeRule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	public static TestRule forTest(final TestSession session)
	{
		return new NavigateToHomeRule(session);
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement() {

			@Override
			public void evaluate() throws Throwable
			{
				pollServer();
				session.webDriver().navigate().to(session.pantheistUrl());
				base.evaluate();
			}
		};
	}

	private void pollServer() throws InterruptedException
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
	}
}
