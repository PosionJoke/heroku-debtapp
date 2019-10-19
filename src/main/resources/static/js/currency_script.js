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


