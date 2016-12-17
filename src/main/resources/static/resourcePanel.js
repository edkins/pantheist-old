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
		var path = $('#resourcePanel #path').val();
		actions.delete(path)
			.then( resourcePanel.hideFailureMessage )
			.catch( resourcePanel.showFailureMessage );
	},
	
	show: function(type,path,name)
	{
		$('#resourcePanel').css('display','block');
		$('#resourcePanel #typeName').text(type);
		$('#resourcePanel #name').text(name);
		$('#resourcePanel #path').val(path);
		resourcePanel.hideFailureMessage();
	}
};
