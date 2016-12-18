resourcePanel = {
	showFailureMessage: function()
	{
		$('#resourcePanel #failureMessage').text('Failed.');
	},
	
	hideFailureMessage: function()
	{
		$('#resourcePanel #failureMessage').text('');
	},
	
	deleteResourceClick: function(event)
	{
		actions.deleteResource(resourcePanel.resourceType(), resourcePanel.resourceId())
			.then( actions.visitRoot )
			.catch( resourcePanel.showFailureMessage );
	},
	
	show: function(resourceType,resourceId,name)
	{
		$('#resourcePanel').css('display','block');
		$('#resourcePanel #resourceType').val(resourceType);
		$('#resourcePanel #resourceId').val(resourceId);
		$('#resourcePanel #typeName').text(resourceType);
		$('#resourcePanel #name').text(name);
		$('#resourcePanel #componentCreatorType').val('syntax-node-literal');
		resourcePanel.refreshAll();
	},
	
	refreshAll: function()
	{
		resourcePanel.refreshComponentCreator();
		resourcePanel.hideFailureMessage();
		actions.refreshComponents();
	},
	
	refreshComponentCreator: function()
	{
		var componentType = $('#resourcePanel #componentCreatorType').val();
		$('#resourcePanel .component-creator').css('display','none');
		$('#resourcePanel #' + componentType).css('display','block');
		$('#resourcePanel #' + componentType + ' input').val('');
		$('#resourcePanel #createFailureMessage').text('');
	},
	
	showCreateFailureMessage: function()
	{
		$('#resourcePanel #createFailureMessage').text('Failed.');
	},
	
	componentCreatorData: function()
	{
		var creatorId = $('#resourcePanel #componentCreatorType').val();
		var p = $('#resourcePanel #' + creatorId);
		switch( creatorId )
		{
		case 'syntax-node-literal':
			return {
				componentType: 'node',
				request: {
					type: 'literal'
				}
			};
		case 'syntax-node-zero_or_more':
			return {
				componentType: 'node',
				request: {
					type: 'zero_or_more',
					children: [$('input',p).val()]
				}
			};
		case 'syntax-node-one_or_more':
			return {
				componentType: 'node',
				request: {
					type: 'one_or_more',
					children: [$('input',p).val()]
				}
			};
		case 'syntax-node-sequence':
			return {
				componentType: 'node',
				request: {
					type: 'sequence',
					children: $('input',p).val().split(' ')
				}
			};
		case 'syntax-node-choice':
			return {
				componentType: 'node',
				request: {
					type: 'choice',
					children: $('input',p).val().split(' ')
				}
			};
		case 'syntax-token-literal':
			return {
				componentType: 'token',
				request: {
					type: 'literal'
				}
			};
		case 'syntax-token-regex':
			return {
				componentType: 'token',
				request: {
					type: 'regex',
					value: $('input',p).val()
				}
			};
		default:
			console.log("Don't know what to do with " + creatorId);
		}
	},
	
	clickCreateComponent: function(event)
	{
		var data = resourcePanel.componentCreatorData();
		services.putComponent(
			resourcePanel.resourceType(),
			resourcePanel.resourceId(),
			data.componentType,
			$('#resourcePanel #createComponentId').val(),
			data.request
		).then( resourcePanel.refreshAll )
		.catch( resourcePanel.showCreateFailureMessage );
	},
	
	resourceType: function()
	{
		return $('#resourcePanel #resourceType').val();
	},
	
	resourceId: function()
	{
		return $('#resourcePanel #resourceId').val();
	},
	
	componentTableHeadings: function(componentType)
	{
		switch(componentType)
		{
		case 'node': return ['id','type','children'];
		case 'token': return ['id','type','value'];
		default:
			console.log('bad componentType ' + componentType);
		}
	},
	
	clickDeleteComponent: function(event)
	{
		var button = $(event.target);
		services.deleteComponent(
			resourcePanel.resourceType(),
			resourcePanel.resourceId(),
			button.data('componentType'),
			button.data('componentId')
		).then( actions.refreshComponents );
	},
	
	showComponents: function(response)
	{
		$('#resourcePanel #components').text(JSON.stringify(response));
		$('#resourcePanel #components').empty();
		
		for (var i = 0; i < response.length; i++)
		{
			var obj = response[i];
			var h2 = $('<h2>');
			h2.text(obj.componentType);
			$('#resourcePanel #components').append(h2);
			
			var headings = resourcePanel.componentTableHeadings(obj.componentType);
			
			var table = $('<table>');
			var thr = $('<tr>');
			thr.append($('<th>'));
			for (var k = 0; k < headings.length; k++)
			{
				var th = $('<th>');
				th.text(headings[k]);
				thr.append(th);
			}
			table.append(thr);
			
			for (var j = 0; j < obj.components.length; j++)
			{
				var obj2 = obj.components[j];
				var tr = $('<tr>');
				var td_del = $('<td>');
				var inp_del = $('<input>');
				inp_del.attr('type','button');
				inp_del.attr('value','Del');
				inp_del.data('componentType',obj.componentType);
				inp_del.data('componentId',obj2.id);
				inp_del.click(resourcePanel.clickDeleteComponent);
				td_del.append(inp_del);
				tr.append(td_del);
				
				for (var k = 0; k < headings.length; k++)
				{
					var prop = headings[k];
					var td = $('<td>');
					td.text(JSON.stringify(obj2[prop]));
					tr.append(td);
				}
				table.append(tr);
			}
			$('#resourcePanel #components').append(table);
		}
	}
};
