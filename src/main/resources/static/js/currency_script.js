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

		console.log(elementId + " " + sec + " " + isNaN(sec));

		document.getElementById(elementId).innerHTML = numberOfDays + "d "
			+ numberOfHours + "h " + numberOfMinutes + "m " + numberOfSeconds + "s ";

		if (sec <= 0 || sec === " ") {
			if(sec === " "){
				document.getElementById(elementId).innerHTML = " ";
				return
			}
			document.getElementById(elementId).innerHTML = "EXPIRED";
			clearInterval(x);
		}
		sec--;
	}, 1000);
}

function timerList(fieldsArray, secondsArray) {
	fieldsArray.forEach(function (elementId, index) {
		debtTimer(document.getElementById(secondsArray[index]).textContent, elementId);
	});
}

function makeTimerList(countOfRows, prefixTime, prefixSeconds) {
	var timerFieldsArray = [];
	var i;
	for (i = 0; i < countOfRows; i++) {
		timerFieldsArray.push(i + prefixTime);
	}

	var secondsArrays = [];
	var j;
	for (j = 0; j < countOfRows; j++) {
		secondsArrays.push(j + prefixSeconds);
	}

	timerList(timerFieldsArray, secondsArrays);
}


