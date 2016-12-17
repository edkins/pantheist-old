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
	}
};
