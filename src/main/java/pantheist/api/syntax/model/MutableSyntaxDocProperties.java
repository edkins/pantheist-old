package pantheist.api.syntax.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MutableSyntaxDocPropertiesImpl.class)
public interface MutableSyntaxDocProperties extends SyntaxDocProperties
{
	void setRoot(@Nullable final SyntaxDocProperty root);

	void setWhitespace(@Nullable final SyntaxDocProperty whitespace);

}
