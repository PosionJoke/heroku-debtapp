function myFunction(){
	alert("Button 1 pressed");
}

function testFunction() {
	document.getElementById('testJS').innerText = "HELLO from INDEX JS";
}

function filtrCurrency() {
	$("#myInput").on("keyup", function() {
		var value = $(this).val().toLowerCase();
		$(".dropdown-menu li").filter(function() {
			$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		});
	});
}
