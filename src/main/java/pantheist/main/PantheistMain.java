package pantheist.main;

import com.google.inject.Guice;

import pantheist.system.server.PantheistServer;

public class PantheistMain
{
	public static void main(final String[] args)
	{
		Guice.createInjector(new AllPantheistModule()).getInstance(PantheistServer.class).start();
	}
}
