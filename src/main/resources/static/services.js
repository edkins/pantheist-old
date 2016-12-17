services = {
	get: function(path)
	{
		return Promise.resolve($.ajax('/syntax'));
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
					resources: response.resources.map( m => services.level2resource('syntax',m) )
				}
			];
		} );
	},

	level2resource: function(type,metadata)
	{
		return {
			name: metadata.name,
			path: metadata.path,
			level:2,
			type:type,
			resources:[]
		}
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
	}
};
