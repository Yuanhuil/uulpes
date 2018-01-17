
function checkIsNotEmpty(str) {
	if (trim(str) === "") {
		return false;
	} else {
		return true;
	}
}
function notEmpty(id) {
	var val = getValue(id);
	if (trim(val) === "") {
		return false;
	} else {
		return true;
	}
}
function getValue(id) {
	var elem = document.getElementById(id);
	return elem.value;
}
function checkNotSelect(obj) {
	if (1 > obj.value.length) {
		obj.focus();
		return false;
	}
	return true;
}
function trim(str) {  //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function disableall(name) {
	var tags = document.getElementsByName(name);
	for (var i = 0; i < tags.length; i = i + 1) {
		tags[i].disabled = true;
	}
}
function check_date(id) {
	return RegExp("^[0-9]{4,4}-[0-9]{2,2}-[0-9]{2,2}$").test(getValue(id));
}
function check_empty(id) {
	return notEmpty(id);
}


