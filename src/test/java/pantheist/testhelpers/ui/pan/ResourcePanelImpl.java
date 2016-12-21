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
		return el.select().withId("componentCreatorType");
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

	@Override
	public DeletableTable syntaxDoc()
	{
		return interp(el.table().withId("component-table-doc"));
	}

	private DeletableTable interp(final ProtoTable table)
	{
		return new DeletableTableImpl(table.interpret(Column.identifiedBy("id"), Row.THEAD_FIRST));
	}

	@Override
	public TextEntry syntaxDocNodeList()
	{
		return el.p().withId("syntax-doc").inputText().choose();
	}

	@Override
	public TextEntry syntaxNodeRegex()
	{
		return el.p().withId("syntax-node-regex").inputText().choose();
	}

	@Override
	public TextEntry syntaxNodeZeroOrMore()
	{
		return el.p().withId("syntax-node-zero_or_more").inputText().choose();
	}

	@Override
	public TextEntry syntaxNodeOneOrMore()
	{
		return el.p().withId("syntax-node-one_or_more").inputText().choose();
	}

	@Override
	public TextEntry syntaxNodeSequence()
	{
		return el.p().withId("syntax-node-sequence").inputText().choose();
	}

	@Override
	public TextEntry syntaxNodeChoice()
	{
		return el.p().withId("syntax-node-choice").inputText().choose();
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
	public Textual trySyntaxResult()
	{
		el.allowTimeToStabilize();
		return el.p().withId("whatHappened");
	}

}
