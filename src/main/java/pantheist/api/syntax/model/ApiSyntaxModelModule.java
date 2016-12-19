package pantheist.api.syntax.model;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ApiSyntaxModelModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(SyntaxModelFactory.class);
		install(new FactoryModuleBuilder()
				.implement(TryOutTextReport.class, TryOutTextReportImpl.class)
				.build(SyntaxModelFactory.class));
	}

}
