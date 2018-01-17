function moveUp(selectionId, recorderId) {
	var selection = document.getElementById(selectionId);
	if (moveUpHelper(selection)) {
		var recorder = document.getElementById(recorderId);
		updateRecorder(selection, recorder);
	}
}
function moveUpHelper(box) {
	var dirty = false;
	for (var i = 0; i < box.options.length; i=i+1) {
		if (box.options[i].selected && i > 0) {
			if (!box.options[i - 1].selected) {
				box.insertBefore(box.options[i], box.options[i - 1]);
				dirty = true;
			}
		}
	}
	return dirty;
}
function moveDown(selectionId, recorderId) {
	var selection = document.getElementById(selectionId);
	if (moveDownHelper(selection)) {
		var recorder = document.getElementById(recorderId);
		updateRecorder(selection, recorder);
	}
}
function moveDownHelper(box) {
	var dirty = false;
	for (var i = box.options.length - 1; i >= 0; i=i-1) {
		if (box.options[i].selected && i < box.options.length - 1) {
			if (!box.options[i + 1].selected) {
				box.insertBefore(box.options[i + 1], box.options[i]);
				dirty = true;
			}
		}
	}
	return dirty;
}
function updateRecorder(selection, recorder) {
	recorder.value = "";
	for (var i = 0; i < selection.options.length; i=i+1) {
		recorder.value = recorder.value + selection.options[i].value;
		if (i + 1 < selection.options.length) {
			recorder.value = recorder.value + ",";
		}
	}
	if (recorder.onchange !== null) {
		recorder.onchange();
	}
}


