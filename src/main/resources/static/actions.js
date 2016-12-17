actions = {
	createEmptyResource: function (type,id)
	{
		return services.putEmpty(type,id).then( actions.refreshResources );
	},

	refreshResources: function()
	{
		return services.getResources().then( sidebar.showResources );
	},
	
	refreshComponents: function()
	{
		return services
			.getComponents( resourcePanel.resourceType(), resourcePanel.resourceId() )
			.then( resourcePanel.showComponents );
	},
	
	deleteResource: function(resourceType,resourceId)
	{
		var path = resourceType + '/' + resourceId;
		return services.delete(path).then( actions.refreshResources );
	},

	visitRoot: function()
	{
		$('.main-panel').css('display','none');
	},

	visitResourceType: function(resourceType, name)
	{
		$('.main-panel').css('display','none');
		resourceTypePanel.show(resourceType,name);
	},

	visitResource: function(resourceType, resourceId, name)
	{
		$('.main-panel').css('display','none');
		resourcePanel.show(resourceType, resourceId, name);
	}
};
