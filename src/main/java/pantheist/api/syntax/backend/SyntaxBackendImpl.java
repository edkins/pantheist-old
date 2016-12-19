package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.error.ParserException;

import pantheist.api.generic.store.ResourceStore;
import pantheist.api.generic.store.ResourceStoreSession;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxModelFactory;
import pantheist.api.syntax.model.TryOutTextReport;
import pantheist.common.except.NotFoundException;
import pantheist.engine.parser.ParserEngine;

final class SyntaxBackendImpl implements SyntaxBackend
{
	private static final Logger LOGGER = LogManager.getLogger(SyntaxBackendImpl.class);
	private final ParserEngine engine;
	private final ResourceStore store;
	private final SyntaxModelFactory modelFactory;

	@Inject
	SyntaxBackendImpl(final ParserEngine engine, final ResourceStore store, final SyntaxModelFactory modelFactory)
	{
		this.engine = checkNotNull(engine);
		this.store = checkNotNull(store);
		this.modelFactory = checkNotNull(modelFactory);
	}

	private Syntax fetchSyntax(final String syntaxId) throws NotFoundException
	{
		try (ResourceStoreSession session = store.open())
		{
			return (Syntax) session.resource("syntax", syntaxId);
		}

	}

	@Override
	public TryOutTextReport tryOutText(final String syntaxId, final String text) throws NotFoundException
	{
		final Parser<?> parser = engine.jparsecForSyntax(fetchSyntax(syntaxId));

		try
		{
			final Object result = parser.parse(text);

			return modelFactory.tryOutTextReport("It returned an object " + result);
		}
		catch (final ParserException e)
		{
			LOGGER.catching(e);
			return modelFactory.tryOutTextReport(e.getMessage());
		}
	}

}
