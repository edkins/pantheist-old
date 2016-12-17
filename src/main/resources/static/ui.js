function level2resource(type,metadata)
{
	return {
		name: metadata.name,
		path: metadata.path,
		level:2,
		type:type,
		resources:[]
	}
}

function getResources()
{
	return $.ajax('/syntax').then( response => {
		return [
			{
				name:"Syntax",
				path:"/syntax",
				level:1,
				type:"syntax",
				resources: response.resources.map( m => level2resource('syntax',m) )
			}
		];
	} );
}

function visit(type, level, path, name)
{
	$('.main-panel').css('display','none');
	switch(level)
	{
	case 1:
		$('#resourceTypePanel').css('display','block');
		$('#resourceTypePanel #typeName').text(name);
		$('#resourceTypePanel #type').val(type);
		$('#resourceTypePanel #failureMessage').text('');
		break;
	case 2:
		$('#resourcePanel').css('display','block');
		$('#resourcePanel #typeName').text(type);
		$('#resourcePanel #name').text(name);
		$('#resourcePanel #path').val(path);
		$('#resourcePanel #failureMessage').text('');
		break;
	}
}

function showResourceTypeFailureMessage()
{
	$('#resourceTypePanel #failureMessage').text('Failed.');
}

function showResourceFailureMessage()
{
	$('#resourcePanel #failureMessage').text('Failed.');
}

function createEmptyResource(type,id)
{
	var path = '/' + type + '/' + id;
	return $.ajax( path,
		{
			method:'PUT',
			contentType:'application/json',
			data:'{}',
			dataType:'text'
		}).then( () => listResources(), () => showResourceTypeFailureMessage() );
}

function resourceClick(event)
{
	var el = $(event.target);
	$('#resources a').attr('class','');
	el.attr('class','active');
	visit(el.data('type'), el.data('level'), el.data('path'), el.data('name') );
}

function createResourceClick(event)
{
	var type = $('#resourceTypePanel #type').val();
	var id = $('#resourceTypePanel #name').val();
	createEmptyResource(type,id);
}

function deleteResourceClick(event)
{
	var path = $('#resourcePanel #path').val();
	$.ajax( path,
		{
			method:'DELETE',
			dataType:'text'
		}).then( () => listResources(), () => showResourceFailureMessage() );
}

function makeA(obj)
{
	var a = $('<a>');
	a.text(obj.name);
	a.attr('href','#');
	a.data('type',obj.type);
	a.data('level',obj.level);
	a.data('path',obj.path);
	a.data('name',obj.name);
	a.click(resourceClick);
	return a;
}

function listResources()
{
	getResources().then( response => {
		$('#resources').empty();
		
		for (var i = 0; i < response.length; i++)
		{
			var obj = response[i];
			var li = $('<li>');
			li.append(makeA(obj));
			var ul2 = $('<ul>');
			for (var j = 0; j < obj.resources.length; j++)
			{
				var obj2 = obj.resources[j];
				var li2 = $('<li>');
				li2.append(makeA(obj2));
				ul2.append(li2);
			}
			li.append(ul2);
			$('#resources').append(li);
		}
	});
}
