package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.Column;
import pantheist.testhelpers.ui.generic.ContainerElement;
import pantheist.testhelpers.ui.generic.InterpretedTable;
import pantheist.testhelpers.ui.generic.Menu;
import pantheist.testhelpers.ui.generic.Row;
import pantheist.testhelpers.ui.generic.TextEntry;

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
		return el.inputButton().withValue("Delete").choose();
	}

	@Override
	public Menu syntaxCreateType()
	{
		return el.select().withId("componentCreatorType").choose();
	}

	@Override
	public ClickableText syntaxCreateButton()
	{
		return el.inputButton().withValue("Create").choose();
	}

	@Override
	public InterpretedTable syntaxNodes()
	{
		return el.table().withId("component-table-node")
				.choose()
				.interpret(Column.identifiedBy("id"), Row.THEAD_FIRST);
	}

	@Override
	public TextEntry syntaxCreateName()
	{
		return el.inputText().withId("createComponentId").choose();
	}

}
