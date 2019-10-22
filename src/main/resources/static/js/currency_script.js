function filterCurrencyDropDown() {
	$("#myInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$(".dropdown-menu li").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
}

function filterTable(){
	$("#tableFilterInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$("#tableFilter tr").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
}

function debtTimer(sec, elementId){
	var x = setInterval(function() {

		var numberOfDays = Math.floor(sec / 86400);
		var numberOfHours = Math.floor((sec % 86400 ) / 3600) ;
		var numberOfMinutes = Math.floor(((sec % 86400 ) % 3600 ) / 60);
		var numberOfSeconds = Math.floor(((sec % 86400 ) % 3600 ) % 60);

		document.getElementById(elementId).innerHTML = numberOfDays + "d "
			+ numberOfHours + "h " + numberOfMinutes + "m " + numberOfSeconds + "s ";
		if (sec < 0) {
			clearInterval(x);
			document.getElementById(elementId).innerHTML = "EXPIRED";
		}
		sec--;
	}, 1000);
}

function timmerList(fieldsArray, sec) {
	fieldsArray.forEach(function (elementId) {
		debtTimer(sec, elementId);
	})
}


