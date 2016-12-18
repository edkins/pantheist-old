services = {
	get: function(path)
	{
		return Promise.resolve($.ajax(path));
	},

	getResources: function()
	{
		return services.get('/syntax').then( response => {
			return [
				{
					name:"Syntax",
					path:"/syntax",
					level:1,
					type:"syntax",
					resources: response.resources
				}
			];
		} );
	},
	
	componentTypes: function(type)
	{
		switch(type)
		{
		case 'syntax': return ['node'];
		default:
			console.log('bad type ' + type);
		}
	},

	getComponents: function(resourceType,resourceId)
	{
		return Promise.all(services.componentTypes(resourceType).map( componentType => {
			return services.get('/'+resourceType+'/'+resourceId+'/'+componentType).then( response => {
				return {
					componentType: componentType,
					components: response.components
				};
			});
		} ));
	},
	
	putEmpty: function(type,id)
	{
		var path = '/' + type + '/' + id;
		return Promise.resolve($.ajax( path,
					{
						method:'PUT',
						contentType:'application/json',
						data:'{}'
					}));
	},
	
	delete: function(path)
	{
		return Promise.resolve($.ajax( path,
			{
				method:'DELETE'
			}));
	},
	
	deleteComponent: function(resourceType,resourceId,componentType,componentId)
	{
		var path = '/' + resourceType + '/' + resourceId + '/' + componentType + '/' + componentId;
		return services.delete(path);
	},
	
	putComponent: function(resourceType,resourceId,componentType,componentId,request)
	{
		var path = '/' + resourceType + '/' + resourceId + '/' + componentType + '/' + componentId;
		return Promise.resolve($.ajax( path,
			{
				method: 'PUT',
				contentType: 'application/json',
				data: JSON.stringify(request)
			}));
	}
};
