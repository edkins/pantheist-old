package pantheist.api.syntax.model;

import com.google.inject.assistedinject.Assisted;

public interface SyntaxModelFactory
{
	TryOutTextReport tryOutTextReport(@Assisted("whatHappened") String whatHappened);
}
