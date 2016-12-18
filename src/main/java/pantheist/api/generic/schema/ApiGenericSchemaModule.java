package pantheist.api.generic.schema;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class ApiGenericSchemaModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(TypeKnower.class);
		bind(TypeKnower.class).to(TypeKnowerImpl.class).in(Scopes.SINGLETON);
	}

}
