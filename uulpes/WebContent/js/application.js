$.app = {
	waiting : function(message, isSmall) {
		if (!message) {
			message = "装载中...";
		}

		message = '<img src="' + ctx + '/images/loading.gif" '
				+ (isSmall ? "width='20px'" : "") + '/> ' + message;
		if (!isSmall) {
			message = "<h4>" + message + "</h4>";
		}
		$.blockUI({
			fadeIn : 700,
			fadeOut : 700,
			showOverlay : false,
			css : {
				border : 'none',
				padding : '15px',
				backgroundColor : '#eee',
				'-webkit-border-radius' : '10px',
				'-moz-border-radius' : '10px',
				opacity : 1,
				color : '#000',
				width : isSmall ? "40%" : "30%"

			},
			message : message
		});
	},
	waitingOver : function() {
		$.unblockUI();
	}
}