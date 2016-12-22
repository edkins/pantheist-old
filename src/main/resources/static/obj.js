'use strict';
var obj = {
	map: function(data)
	{
		if (Array.isArray(data))
		{
			throw ('Data is an array: ' + JSON.stringify(data));
		}
	
		return {
			at: key => {
				if (key in data)
				{
					return obj.wrap(data[key]);
				}
				else
				{
					return obj.nothing();
				}
			}
		};
	},
	
	nothing: function() {
		return {
			at: key => {
				return obj.nothing();
			},

			singleOrNull: () => {
				return null;
			},
			
			find: (keyName,keyValue) => {
				return obj.nothing();
			}
		};
	},
	
	wrap: function(data)
	{
		if (data === null || data === undefined)
		{
			return obj.nothing();
		}
		else if (Array.isArray(data))
		{
			return obj.list(data);
		}
		else if (typeof(data) == 'object')
		{
			return obj.map(data);
		}
		else
		{
			throw ('Cannot wrap object of unknown type: ' + typeof(data));
		}
	},

	list: function(data)
	{
		if (!Array.isArray(data))
		{
			throw ('Data is not an array: ' + JSON.stringify(data));
		}
	
		return {
			at: key => {
				throw ('Cannot call .at() on list ' + JSON.stringify(data));
			},

			find: (keyName,keyValue) => {
				for (var i = 0; i < data.length; i++)
				{
					if (data[i][keyName] === keyValue)
					{
						return obj.wrap(data[i]);
					}
				}
				return obj.nothing();
			},
			
			singleOrNull: function () {
				if (data.length == 0) {
					return null;
				}
				else if (data.length == 1) {
					return data[0];
				}
				else {
					throw ('List is not a singleton: ' + JSON.stringify(data));
				}
			}
			
		};
	}
};
