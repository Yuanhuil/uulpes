
function chkedOptCountIn(chkname, startidx, endidx) {
	var chkopts = document.getElementsByName(chkname);
	var count = 0;
	for (var i = startidx; i < endidx; i++) {
		if (chkopts[i].checked) {
			count = count + 1;
		}
	}
	return count;
}
function notGreatThen(chkname, curridx, startidx, endidx, n) {
	var count = chkedOptCountIn(chkname, startidx, endidx);
	if (count > n) {
		var chkopts = document.getElementsByName(chkname);
		chkopts[curridx].checked = false;
		return false;
	}
	return true;
}
function validateAtLeastOne(chkname, startidx, endidx, msg) {
	if (!atLeastOne(chkname, startidx, endidx)) {
		alert("[" + msg + "]\u9009\u9879\u6ca1\u6709\u88ab\u9009\u62e9\uff01");
		return false;
	}
	return true;
}
function atLeastOne(chkname, startidx, endidx) {
	var conut = chkedOptCountIn(chkname, startidx, endidx);
	if (conut < 1) {
		return false;
	}
	return true;
}
function validateNotGreatThen(chkname, curridx, startidx, endidx, n, msg) {
	if (!notGreatThen(chkname, curridx, startidx, endidx, n)) {
		alert("[" + msg + "]\u9009\u9879\u4e0d\u80fd\u5927\u4e8e" + n);
		return false;
	}
	return true;
}
function unchkOthers(chkname, curridx, startidx, endidx) {
	var chkopts = document.getElementsByName(chkname);
	for (var i = startidx; i < endidx; i++) {
		if (curridx != i) {
			chkopts[i].checked = false;
		}
	}
}
function getOptvals(chkname) {
	var arr = new Array();
	var chkopts = document.getElementsByName(chkname);
	for (var i = 0; i < chkopts.length; i++) {
		if (chkopts[i].checked) {
			arr.push(chkopts[i].value);
		}
	}
	return arr.join(",");
}
function chkedOptCount(chkname) {
	var chkopts = document.getElementsByName(chkname);
	var len = chkopts.length;
	var count = 0;
	for (var i = 0; i < len; i = i + 1) {
		if (chkopts[i].checked) {
			count = count + 1;
		}
	}
	return count;
}
function selectOpt(chkname, recorderId) {
	var chkopts = document.getElementsByName(chkname);
	var recorder = document.getElementById(recorderId);
	updateChkoptRecorder(chkopts, recorder);
}
function updateChkoptRecorder(chkopts, recorder) {
	recorder.value = "";
	var array = new Array();
	for (var i = 0; i < chkopts.length; i = i + 1) {
		if (chkopts[i].checked) {
			array.push(chkopts[i].value);
		}
	}
	recorder.value = array.join(",");
	if (recorder.onchange !== null) {
		recorder.onchange();
	}
}
function selectItem(selId, recordId) {
	var o1 = document.getElementById(selId);
	var o2 = document.getElementById(recordId);
	o2.value = o1.value;
}
function clearText(id) {
	var elem = document.getElementById(id);
	elem.value = "";
}
function disabledElem(id) {
	var elem = document.getElementById(id);
	elem.disabled = true;
}
function enabledElem(id) {
	var elem = document.getElementById(id);
	elem.disabled = false;
}
function ifChecked(id) {
	var elem = document.getElementById(id);
	return elem.checked;
}

