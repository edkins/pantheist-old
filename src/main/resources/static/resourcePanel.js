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
		$('#resourcePanel #createComponentType').val('syntax-node-literal');
		resourcePanel.refreshAll();
	},
	
	refreshAll: function()
	{
		resourcePanel.refreshComponentCreator();
		resourcePanel.hideFailureMessage();
		actions.refreshComponents();
		$('#resourcePanel input[type=text]').val('');
		$('#syntaxResourceDiv #whatHappened').text('');
		$('#resourcePanel #createFailureMessage').text('');
		$('#text-to-try').val('');
	},
	
	messageForCreate(creatorId)
	{
		switch(creatorId)
		{
			case 'syntax-node-literal':
				return 'In the case of literal tokens, the name is also the sequence of characters to match.';
			case 'syntax-node-single_character':
				return 'Enter space-separated list of characters to match, or special values: space newline tab digit latin_lower latin_upper visible_ascii:';
			case 'syntax-node-glued_zero_or_more':
			case 'syntax-node-zero_or_more':
				return 'Occurrences of this node consist of zero or more occurrences of:';
			case 'syntax-node-glued_one_or_more':
			case 'syntax-node-one_or_more':
				return 'Occurrences of this node consist of one or more occurrences of:';
			case 'syntax-node-glued_sequence':
			case 'syntax-node-sequence':
				return 'Enter a space-separated sequence of nodes that will be matched in order:';
			case 'syntax-node-choice':
				return 'Enter a space-separated sequence of nodes to choose from:';
			case 'syntax-doc':
				return 'Enter root or whitespace node list:';
			default:
				throw ('Unrecognized type: '+creatorId);
		}
	},
	
	detailBoxVisibility(creatorId)
	{
		switch(creatorId)
		{
			case 'syntax-node-literal':
				return 'hidden';
			default:
				return 'visible';
		}
	},
	
	exceptionsBoxVisibility(creatorId)
	{
		switch(creatorId)
		{
			case 'syntax-node-single_character':
				return 'visible';
			default:
				return 'hidden';
		}
	},
	
	refreshComponentCreator: function()
	{
		var creatorId = $('#resourcePanel #createComponentType').val();
		
		$('#resourcePanel #createComponentHelpMessage').text(resourcePanel.messageForCreate(creatorId));
		$('#resourcePanel #createComponentDetail').val('');
		$('#resourcePanel #createComponentDetail').css('visibility',resourcePanel.detailBoxVisibility(creatorId));
		$('#resourcePanel #createComponentExceptions').val('');
		$('#resourcePanel #createComponentExceptionsSection').css('visibility',resourcePanel.exceptionsBoxVisibility(creatorId));
	},
	
	showCreateFailureMessage: function()
	{
		$('#resourcePanel #createFailureMessage').text('Failed.');
	},
	
	split: function(str)
	{
		if (str == undefined || str == '')
		{
			return [];
		}
		return str.split(' ');
	},
	
	componentCreatorData: function(componentId)
	{
		var creatorId = $('#resourcePanel #createComponentType').val();
		var splitDetail = resourcePanel.split($('#resourcePanel #createComponentDetail').val());
		var splitExceptions = resourcePanel.split($('#resourcePanel #createComponentExceptions').val());
		var lastPart = creatorId.substring(creatorId.lastIndexOf('-')+1);
		switch( creatorId )
		{
		case 'syntax-node-literal':
			return {
				componentType: 'node',
				request: {
					type: lastPart,
					value: componentId
				}
			};
		case 'syntax-node-single_character':
			return {
				componentType: 'node',
				request: {
					type: lastPart,
					children: splitDetail,
					exceptions: splitExceptions
				}
			};
		case 'syntax-node-glued_zero_or_more':
		case 'syntax-node-glued_one_or_more':
		case 'syntax-node-glued_sequence':
		case 'syntax-node-zero_or_more':
		case 'syntax-node-one_or_more':
		case 'syntax-node-sequence':
		case 'syntax-node-choice':
			return {
				componentType: 'node',
				request: {
					type: lastPart,
					children: splitDetail
				}
			};
		case 'syntax-doc':
			return {
				componentType: 'doc',
				request: {
					children: splitDetail
				}
			};
		default:
			console.log("Don't know what to do with " + creatorId);
		}
	},
	
	clickCreateComponent: function(event)
	{
		var componentId = $('#resourcePanel #createComponentId').val();
		if (componentId == '')
		{
			$('#resourcePanel #createFailureMessage').text('Must enter name');
			return;
		}
		var data = resourcePanel.componentCreatorData(componentId);
		services.createComponent(
			resourcePanel.resourceType(),
			resourcePanel.resourceId(),
			data.componentType,
			componentId,
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
		case 'node': return [
				['type',x=>x],
				['value',x => x==null ? '' : x],
				['children',x => x.length===0 ? '' : JSON.stringify(x)],
				['exceptions',x => x.length===0 ? '' : JSON.stringify(x)]
			];
		case 'doc': return [
				['children',JSON.stringify]
			];
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
			table.attr('id','component-table-'+obj.componentType);
			var thead = $('<thead>');
			var thr = $('<tr>');
			thr.append($('<th>'));
			var th_id = $('<th>');
			th_id.text('id');
			thr.append(th_id);
			for (var k = 0; k < headings.length; k++)
			{
				var th = $('<th>');
				th.text(headings[k][0]);
				thr.append(th);
			}
			thead.append(thr);
			table.append(thead);
			
			var tbody = $('<tbody>');

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
				
				var td_id = $('<td>');
				td_id.text(obj2.id);
				tr.append(td_id);
				for (var k = 0; k < headings.length; k++)
				{
					var prop = headings[k][0];
					var fn = headings[k][1];
					var td = $('<td>');
					td.text(fn(obj2.data[prop]));
					tr.append(td);
				}
				tbody.append(tr);
			}
			table.append(tbody);
			$('#resourcePanel #components').append(table);
		}
	},
	
	clickTrySyntax: function(event)
	{
	$('#resourcePanel #createFailureMessage').text('Clicked on try.')
		var text = $('#syntaxResourceDiv #text-to-try').val();
		services.trySyntax(resourcePanel.resourceId(),text)
			.then( report => $('#syntaxResourceDiv #whatHappened').text( report.whatHappened ) )
			.catch( () => $('#syntaxResourceDiv #whatHappened').text('Failed.') );
	}
};
