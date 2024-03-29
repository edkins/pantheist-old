'use strict';
var services = {
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
		case 'syntax': return ['node','doc','operator'];
		default:
			console.log('bad type ' + type);
		}
	},
	
	escape: function(segment)
	{
		if (typeof(segment) !== 'string' || segment === '')
		{
			throw 'Must be nonempty string';
		}
		else if (segment === '.')
		{
			return '%2E';
		}
		else if (segment === '..')
		{
			return '%2E%2E';
		}
		else
		{
			return encodeURIComponent(segment);
		}
	},
	
	resourcePath: function(resourceType,resourceId)
	{
		return '/' + services.escape(resourceType) + '/' + services.escape(resourceId);
	},
	
	componentTypePath: function(resourceType,resourceId,componentType)
	{
		return services.resourcePath(resourceType,resourceId) + '/' + services.escape(componentType);
	},

	componentPath: function(resourceType,resourceId,componentType,componentId)
	{
		return services.componentTypePath(resourceType,resourceId,componentType) + '/' + services.escape(componentId);
	},

	getComponents: function(resourceType,resourceId)
	{
		return Promise.all(services.componentTypes(resourceType).map( componentType => {
			return services.get(services.componentTypePath(resourceType,resourceId,componentType)).then( response => {
				return {
					componentType: componentType,
					components: response.components
				};
			});
		} ));
	},
	
	putEmpty: function(resourceType,resourceId)
	{
		var path = services.resourcePath(resourceType,resourceId);
		return Promise.resolve($.ajax( path,
					{
						method:'PUT',
						contentType:'application/json',
						data:'{}'
					}));
	},
	
	deleteResource: function(resourceType,resourceId)
	{
		var path = services.resourcePath(resourceType,resourceId);
		return Promise.resolve($.ajax( path,
			{
				method:'DELETE'
			}));
	},
	
	deleteComponent: function(resourceType,resourceId,componentType,componentId)
	{
		var path = services.componentPath(resourceType,resourceId,componentType,componentId);
		return Promise.resolve($.ajax( path,
			{
				method:'DELETE'
			}));
	},
	
	putComponent: function(resourceType,resourceId,componentType,componentId,request,allowReplace)
	{
		var path = services.componentPath(resourceType,resourceId,componentType,componentId);
		var headers = {};
		if (allowReplace)
		{
			headers['X-Allow-Replace'] = 'true';
		}
		return Promise.resolve($.ajax( path,
			{
				method: 'PUT',
				contentType: 'application/json',
				data: JSON.stringify(request),
				headers: headers
			}));
	},

	trySyntax: function(syntaxId,text)
	{
		var path = services.resourcePath('syntax',syntaxId) + '/try';
		return Promise.resolve($.ajax(path,
			{
				method: 'POST',
				contentType: 'text/plain',
				data: text
			}));
	}
};
