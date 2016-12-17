package pantheist.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;

final class EmptyObjectImpl implements EmptyObject
{
	@JsonCreator
	EmptyObjectImpl()
	{

	}
}
