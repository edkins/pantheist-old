var sidebar = {

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
	},
	
	resourceClick: function(event)
	{
		var el = $(event.target);
		$('#resources a').attr('class','');
		el.attr('class','active');
		sidebar.visit(el.data('type'), el.data('level'), el.data('path'), el.data('name') );
	},
	
	makeA: function(obj)
	{
		var a = $('<a>');
		a.text(obj.name);
		a.attr('href','#');
		a.data('type',obj.type);
		a.data('level',obj.level);
		a.data('path',obj.path);
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
			li.append(sidebar.makeA(obj));
			var ul2 = $('<ul>');
			for (var j = 0; j < obj.resources.length; j++)
			{
				var obj2 = obj.resources[j];
				var li2 = $('<li>');
				li2.append(sidebar.makeA(obj2));
				ul2.append(li2);
			}
			li.append(ul2);
			$('#resources').append(li);
		}
	}
};
