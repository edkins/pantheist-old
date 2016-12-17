resourceTypePanel = {
	showFailureMessage: function()
	{
		$('#resourceTypePanel #failureMessage').text('Failed.');
	},
	
	hideFailureMessage: function()
	{
		$('#resourceTypePanel #failureMessage').text('');
	},
	
	clearForm: function()
	{
		$('#resourceTypePanel #name').val('');
		resourceTypePanel.hideFailureMessage();
	},

	createResourceClick: function(event)
	{
		var type = $('#resourceTypePanel #type').val();
		var id = $('#resourceTypePanel #name').val();
		actions.createEmptyResource(type,id)
			.then( resourceTypePanel.clearForm )
			.catch( resourceTypePanel.showFailureMessage );
	},
	
	show: function(type,name)
	{
		$('#resourceTypePanel').css('display','block');
		$('#resourceTypePanel #typeName').text(name);
		$('#resourceTypePanel #type').val(type);
		resourceTypePanel.clearForm();
	}
};
