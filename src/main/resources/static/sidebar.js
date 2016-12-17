var sidebar = {

	resourceTypeClick: function(event)
	{
		var el = $(event.target);
		$('#resources a').attr('class','');
		el.attr('class','active');
		actions.visitResourceType(el.data('resource-type'), el.data('name') );
	},
	
	resourceClick: function(event)
	{
		var el = $(event.target);
		$('#resources a').attr('class','');
		el.attr('class','active');
		actions.visitResource(el.data('resourceType'), el.data('resourceId'), el.data('name') );
	},
	
	makeResourceTypeA: function(obj)
	{
		var a = $('<a>');
		a.text(obj.name);
		a.attr('href','#');
		a.data('resourceType',obj.type);
		a.data('name',obj.name);
		a.click(sidebar.resourceTypeClick);
		return a;
	},
	
	makeResourceA: function(resourceType, obj)
	{
		var a = $('<a>');
		a.text(obj.name);
		a.attr('href','#');
		a.data('resourceType',resourceType);
		a.data('resourceId',obj.id);
		a.data('name',obj.name);
		a.click(sidebar.resourceClick);
		return a;
	},
	
	showResources: function(response)
	{
		$('#resources').empty();
		
		for (var i = 0; i < response.length; i++)
		{
			var obj = response[i];
			var li = $('<li>');
			li.append(sidebar.makeResourceTypeA(obj));
			var ul2 = $('<ul>');
			for (var j = 0; j < obj.resources.length; j++)
			{
				var obj2 = obj.resources[j];
				var li2 = $('<li>');
				li2.append(sidebar.makeResourceA(obj.type, obj2));
				ul2.append(li2);
			}
			li.append(ul2);
			$('#resources').append(li);
		}
	}
};
