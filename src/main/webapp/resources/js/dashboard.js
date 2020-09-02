const itemTypeSelect = document.getElementById('itemType');
itemType.onchange = function() {
	const itemType = itemTypeSelect.value;
	if (itemType === 'task' || itemType === 'project') {
		datePicker.hidden = false;
	} else {
		datePicker.hidden = true;
	}
}

const datePicker = document.getElementById('date');
datePicker.hidden = true;