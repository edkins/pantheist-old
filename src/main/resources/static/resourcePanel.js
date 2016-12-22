'use strict';
var resourcePanel = {
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
				return 'Occurwindow.rences of this node consist of one or more occurrences of:';
			case 'syntax-node-glued_sequence':
			case 'syntax-node-sequence':
				return 'Enter a space-separated sequence of nodes that will be matched in order:';
			case 'syntax-node-choice':
				return 'Enter a space-separated sequence of nodes to choose from:';
			case 'syntax-operator-infixl':
				return 'Level (higher is tighter, e.g. *) (lower is looser, e.g. +)';
			default:
				throw ('Unrecognized type: '+creatorId);
		}
	},

	furtherDetailHelpMessage(creatorId)
	{
		switch(creatorId)
		{
			case 'syntax-node-single_character':
				return 'Exceptions:';
			case 'syntax-operator-infixl':
				return 'Contained in nodeId:';
			default:
				return undefined;
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
	
	furtherDetailVisibility(creatorId)
	{
		if (resourcePanel.furtherDetailHelpMessage(creatorId) === undefined)
		{
			return 'hidden';
		}
		else
		{
			return 'visible';
		}
	},
	
	refreshComponentCreator: function()
	{
		var creatorId = $('#resourcePanel #createComponentType').val();
		
		$('#resourcePanel #createComponentHelpMessage').text(resourcePanel.messageForCreate(creatorId));
		$('#resourcePanel #createComponentDetail').val('');
		$('#resourcePanel #createComponentDetail').css('visibility',resourcePanel.detailBoxVisibility(creatorId));
		$('#resourcePanel #createComponentDetail2').val('');
		$('#resourcePanel #createComponentFurtherDetailSection').css('visibility',resourcePanel.furtherDetailVisibility(creatorId));
		$('#resourcePAnel #createComponentFurtherDetailHelpMessage').text(resourcePanel.furtherDetailHelpMessage(creatorId));
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
		var splitDetail2 = resourcePanel.split($('#resourcePanel #createComponentDetail2').val());
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
					exceptions: splitDetail2
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
		case 'syntax-operator-infixl':
			return {
				componentType: 'operator',
				request: {
					type: lastPart,
					operator: componentId,
					level: parseInt(splitDetail[0]),
					containedIn: splitDetail2[0]
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
		services.putComponent(
			resourcePanel.resourceType(),
			resourcePanel.resourceId(),
			data.componentType,
			componentId,
			data.request,
			false
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
		case 'node':
			return ['','id','type','value','children','exceptions','root','delim'];
		case 'operator':
			return ['','id','operator','type','level','containedIn'];
		case 'doc':
			return undefined;
		default:
			console.log('bad componentType ' + componentType);
		}
	},
	
	componentTableItems: function(componentType,x,globalStuff)
	{
		switch(componentType)
		{
		case 'node': return [
			create.button('Del', resourcePanel.clickDeleteComponent, {'component-type':componentType,'component-id':x.id}),
			x.id,
			x.data.type,
			create.maybe_string(x.data.value),
			create.maybe_json(x.data.children),
			create.maybe_json(x.data.exceptions),
			create.radio('root', x.id, globalStuff.root, resourcePanel.clickRoot),
			create.radio('delim', x.id, globalStuff.delim, resourcePanel.clickDelim)
			];
		case 'operator': return [
			create.button('Del', resourcePanel.clickDeleteComponent, {'component-type':componentType,'component-id':x.id}),
			x.id,
			x.data.operator,
			x.data.type,
			''+x.data.level,
			x.data.containedIn
			];
		default:
			console.log('bad componentType ' + componentType);
		}
	},
	
	clickRoot: function(event)
	{
		var nodeId = $(event.target).val();
		services.putComponent('syntax',resourcePanel.resourceId(),'doc','root',{children:[nodeId]},true);
	},
	
	clickDelim: function(event)
	{
		var nodeId = $(event.target).val();
		services.putComponent('syntax',resourcePanel.resourceId(),'doc','whitespace',{children:[nodeId]},true);
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
	
	getGlobalStuff(response) {
		var doc = obj.list(response).find('componentType','doc').at('components');
		return {
			root:
				doc.find('id','root')
					.at('data')
					.at('children')
					.singleOrNull(),
			delim:
				doc.find('id','whitespace')
					.at('data')
					.at('children')
					.singleOrNull()
		};
	},
	
	showComponents: function(response)
	{
		$('#resourcePanel #components').text(JSON.stringify(response));
		$('#resourcePanel #components').empty();
		
		var globalStuff = resourcePanel.getGlobalStuff(response);
		
		var panel = $('#resourcePanel #components');
		for (var i = 0; i < response.length; i++)
		{
			var obj = response[i];
			
			var headings = resourcePanel.componentTableHeadings(obj.componentType);
			
			if (headings == undefined)
			{
				continue;
			}
			
			panel.append(create.h2(obj.componentType));

			var table = create.table(headings);
			table.attr('id','component-table-'+obj.componentType);
			
			for (var j = 0; j < obj.components.length; j++)
			{
				var obj2 = obj.components[j];
				var items = resourcePanel.componentTableItems(obj.componentType,obj2,globalStuff);
				table.addRow(items);
			}
			panel.append(table);
		}
	},
	
	clickTrySyntax: function(event)
	{
		var text = $('#syntaxResourceDiv #text-to-try').val();
		services.trySyntax(resourcePanel.resourceId(),text)
			.then( report => $('#syntaxResourceDiv #whatHappened').text( report.whatHappened ) )
			.catch( () => $('#syntaxResourceDiv #whatHappened').text('Failed.') );
	}
};
