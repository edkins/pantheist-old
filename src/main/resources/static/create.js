var create = {

	button: function(text,fn,data) {
		var btn = $('<input>');
		btn.attr('type','button');
		btn.attr('value',text);
		btn.click(fn);
		for (key in data)
		{
			btn.attr('data-' + key, data[key]);
		}
		return btn;
	},
	
	radio: function(group,myValue,actualValue,fn) {
		var radio = $('<input>');
		radio.attr('type','radio');
		radio.attr('name',group);
		radio.attr('value',myValue);
		if (actualValue == myValue)
		{
			radio.attr('checked','checked');
		}
		radio.click(fn);
		return radio;
	},
	
	h2: function(text) {
		var h2 = $('<h2>');
		h2.text(text);
		return h2;
	},
	
	table: function(headings) {
		var table = $('<table>');
		table.append(create.thead(headings));
		tbody = $('<tbody>');
		table.append(tbody);
		
		table.addRow = list => tbody.append(create.tr(list));
		return table;
	},
	
	thead: function(list) {
		var thead = $('<thead>');
		var tr = $('<tr>');
		for(var i = 0; i < list.length; i++)
		{
			var td = $('<th>');
			td.append(list[i]);
			tr.append(td);
		}
		thead.append(tr);
		return thead;
	},
	
	tr: function(list) {
		var tr = $('<tr>');
		for(var i = 0; i < list.length; i++)
		{
			var td = $('<td>');
			td.append(list[i]);
			tr.append(td);
		}
		return tr;
	},
	
	maybe_json(obj) {
		if (obj == null || obj.length == 0)
		{
			return '';
		}
		return JSON.stringify(obj);
	},
	
	maybe_string(str) {
		if (str == null)
		{
			return '';
		}
		return str;
	}

};
