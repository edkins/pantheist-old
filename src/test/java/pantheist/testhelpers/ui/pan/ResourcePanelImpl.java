package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.Column;
import pantheist.testhelpers.ui.generic.ContainerElement;
import pantheist.testhelpers.ui.generic.Menu;
import pantheist.testhelpers.ui.generic.ProtoTable;
import pantheist.testhelpers.ui.generic.Row;
import pantheist.testhelpers.ui.generic.TextEntry;
import pantheist.testhelpers.ui.generic.Textual;

final class ResourcePanelImpl implements ResourcePanel
{
	private final ContainerElement el;

	private ResourcePanelImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	static ResourcePanel from(final ContainerElement el)
	{
		return new ResourcePanelImpl(el);
	}

	@Override
	public void assertDisplayed()
	{
		deleteButton().assertText("Delete");
	}

	@Override
	public ClickableText deleteButton()
	{
		return el.inputButton().withValue("Delete");
	}

	@Override
	public Menu syntaxCreateType()
	{
		return el.select().withId("createComponentType");
	}

	@Override
	public ClickableText syntaxCreateButton()
	{
		return el.inputButton().withValue("Create");
	}

	@Override
	public DeletableTable syntaxNodes()
	{
		return interp(el.table().withId("component-table-node"));
	}

	@Override
	public TextEntry syntaxCreateName()
	{
		return el.inputText().withId("createComponentId");
	}

	private DeletableTable interp(final ProtoTable table)
	{
		return new DeletableTableImpl(table.interpret(Column.identifiedBy("id"), Row.THEAD_FIRST));
	}

	@Override
	public TextEntry syntaxCreateDetail()
	{
		return el.inputText().withId("createComponentDetail");
	}

	@Override
	public TextEntry syntaxCreateExceptions()
	{
		return el.inputText().withId("createComponentExceptions");
	}

	@Override
	public TextEntry trySyntaxText()
	{
		return el.textarea().withId("text-to-try");
	}

	@Override
	public ClickableText trySyntaxButton()
	{
		return el.inputButton().withValue("Try");
	}

	@Override
	public void allowTimeToStabilize()
	{
		el.allowTimeToStabilize();
	}

	@Override
	public Textual trySyntaxResult()
	{
		return el.p().withId("whatHappened");
	}

}
