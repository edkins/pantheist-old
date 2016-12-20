package pantheist.testhelpers.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import pantheist.testhelpers.app.AppRule;
import pantheist.testhelpers.selenium.NavigateToHomeRule;
import pantheist.testhelpers.selenium.WebDriverRule;
import pantheist.testhelpers.ui.pan.PantheistUi;

public class MainRule implements TestRule
{
	private final TestSession session;
	private final RuleChain ruleChain;

	private MainRule(final TestSession session)
	{
		this.session = checkNotNull(session);
		this.ruleChain = RuleChain
				.outerRule(SessionClearingRule.forTest(session))
				.around(new ErrorLoggingRule())
				.around(WebDriverRule.forTest(session))
				.around(AppRule.forTest(session))
				.around(NavigateToHomeRule.forTest(session));
	}

	public static MainRule forNewTest()
	{
		final TestSession session = TestSessionImpl.forNewTest();
		return new MainRule(session);
	}

	public PantheistUi ui()
	{
		return session.ui();
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return ruleChain.apply(base, description);
	}
}
