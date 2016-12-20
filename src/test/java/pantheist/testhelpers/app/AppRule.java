package pantheist.testhelpers.app;

import static com.google.common.base.Preconditions.checkNotNull;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import pantheist.main.AllPantheistModule;
import pantheist.system.server.PantheistServer;
import pantheist.testhelpers.session.TestSession;

public class AppRule implements TestRule
{
	private final TestSession session;

	private AppRule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	public static TestRule forTest(final TestSession session)
	{
		return new AppRule(session);
	}

	private Injector createInjector()
	{
		return Guice.createInjector(Modules.override(new AllPantheistModule()).with(new OverrideModule(session)));
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement() {

			@Override
			public void evaluate() throws Throwable
			{
				try (PantheistServer server = createInjector().getInstance(PantheistServer.class))
				{
					server.start();
					base.evaluate();
				}
			}
		};
	}

}
