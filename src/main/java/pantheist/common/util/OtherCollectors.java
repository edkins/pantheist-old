package pantheist.common.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.google.common.collect.ImmutableSet;

public class OtherCollectors
{
	private OtherCollectors()
	{
		throw new UnsupportedOperationException();
	}

	public static <T> Collector<T, MutableOptional<T>, T> singleton()
	{
		return new SingletonCollector<>();
	}

	private static class SingletonCollector<T> implements Collector<T, MutableOptional<T>, T>
	{

		@Override
		public BiConsumer<MutableOptional<T>, T> accumulator()
		{
			return MutableOptional::add;
		}

		@Override
		public Set<java.util.stream.Collector.Characteristics> characteristics()
		{
			return ImmutableSet.of();
		}

		@Override
		public BinaryOperator<MutableOptional<T>> combiner()
		{
			return (a, b) -> {
				a.add(b);
				return a;
			};
		}

		@Override
		public Function<MutableOptional<T>, T> finisher()
		{
			return MutableOptional::get;
		}

		@Override
		public Supplier<MutableOptional<T>> supplier()
		{
			return MutableOptional::empty;
		}

	}
}
