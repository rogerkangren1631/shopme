var dropDownCountry;
var dataListState;
var fieldState;
$(document).ready(function() {
	dropDownCountry = $("#country");
	dataListState = $("#listStates");
	fieldState = $("#state");

	dropDownCountry.on("change", function() {

		loadStatesForCountry();
		fieldState.val("").focus();
	});

});

function loadStatesForCountry() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_states_by_country/" + selectedCountry.val();
		
	$.get(url, function(responseJSON) {
		dataListState.empty();

		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);

		});
	});

}