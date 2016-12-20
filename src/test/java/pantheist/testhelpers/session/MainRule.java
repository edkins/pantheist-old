package pantheist.testhelpers.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import pantheist.testhelpers.actions.api.PantheistActionsApi;
import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.actions.ui.PantheistActionsUi;
import pantheist.testhelpers.app.AppRule;
import pantheist.testhelpers.app.TempDirRule;
import pantheist.testhelpers.app.WaitForServerRule;
import pantheist.testhelpers.selenium.FirefoxRule;
import pantheist.testhelpers.selenium.NavigateToHomeRule;
import pantheist.testhelpers.ui.pan.PantheistUi;

public class MainRule implements TestRule
{
	private final TestSession session;
	private final TestMode mode;

	private TestRule webDriverRule(final TestMode mode)
	{
		switch (mode) {
		case UI_VISIBLE:
			return FirefoxRule.forTest(session, true);
		case UI_INVISIBLE:
			return FirefoxRule.forTest(session, false);
		default:
			return new NoRule();
		}
	}

	private MainRule(final TestSession session, final TestMode mode)
	{
		this.session = checkNotNull(session);
		this.mode = checkNotNull(mode);
	}

	private RuleChain createRuleChain()
	{
		return RuleChain
				.outerRule(SessionClearingRule.forTest(session))
				.around(new ErrorLoggingRule())
				.around(webDriverRule(mode))
				.around(TempDirRule.forTest(session))
				.around(AppRule.forTest(session))
				.around(WaitForServerRule.forTest(session))
				.around(navigateToHomeRule(session, mode));
	}

	private TestRule navigateToHomeRule(final TestSession session, final TestMode mode)
	{
		switch (mode) {
		case UI_VISIBLE:
		case UI_INVISIBLE:
			return NavigateToHomeRule.forTest(session);
		default:
			return new NoRule();
		}
	}

	public static MainRule forNewTest(final TestMode mode)
	{
		final TestSession session = TestSessionImpl.forNewTest();
		return new MainRule(session, mode);
	}

	public PantheistActions actions()
	{
		switch (mode) {
		case UI_VISIBLE:
		case UI_INVISIBLE:
			return PantheistActionsUi.from(session.ui());
		default:
			return PantheistActionsApi.from(session.pantheistUrl());
		}
	}

	public PantheistUi ui()
	{
		return session.ui();
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return createRuleChain().apply(base, description);
	}
}
