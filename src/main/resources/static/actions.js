actions = {
	createEmptyResource: function (type,id)
	{
		return services.putEmpty(type,id).then( actions.refreshResources );
	},

	refreshResources: function()
	{
		return services.getResources().then( sidebar.showResources );
	},
	
	delete: function(path)
	{
		return services.delete(path).then( actions.refreshResources );
	},

	visitRoot: function()
	{
		$('.main-panel').css('display','none');
	},

	visit: function(type, level, path, name)
	{
		$('.main-panel').css('display','none');
		switch(level)
		{
		case 1:
			resourceTypePanel.show(type,name);
			break;
		case 2:
			resourcePanel.show(type,path,name);
			break;
		}
	}
};
