package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.SyntaxModelFactory;

final class SyntaxBackendImpl implements SyntaxBackend
{
	private final SyntaxModelFactory modelFactory;

	@Inject
	SyntaxBackendImpl(final SyntaxModelFactory modelFactory)
	{
		this.modelFactory = checkNotNull(modelFactory);
	}

	@Override
	public ListSyntaxResponse listSyntax()
	{
		return this.modelFactory.listSyntaxResponse(ImmutableList.of());
	}

}
