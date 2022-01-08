var buttonLoadCountriesAtState;
var dropDownCountry4States;
var dropDownState;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
	buttonLoadCountriesAtState = $("#buttonLoadCountriesForStates");
	dropDownCountry4States = $("#dropDownCountriesForStates");
	dropDownState = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");

	buttonLoadCountriesAtState.click(function() {
		loadCountries4States();
	});

	dropDownCountry4States.on("change", function() {
		loadStates();
	});

	dropDownState.on("change", function() {
		changeFormStateToSelectedState();
	});

	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changedFormStateToNewAtState();
		}
	});

	buttonUpdateState.click(function() {
		updateState();
	});
	
	buttonDeleteState.click(function() {
		deleteState();
	});
});



function deleteState() {
	optionValue = dropDownState.val();
	stateId = optionValue;
	url = contextPath + "states/delete/" + stateId;
		$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function() {
		$("#dropDownStates option[value='" + optionValue + "']").remove();
		changedFormStateToNewAtState();
		showToastMessage("The state has been deleted successfully.")
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});
}


function updateState() {
	if(!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId = dropDownState.val();

	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = { id: stateId, name: stateName, country: { id: countryId, name: countryName } };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownState option:selected").val(stateId);
		$("#dropDownState option:selected").text(stateName);
		showToastMessage("The new state has been updated. ");

		changedFormStateToNewAtState();
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});

}

function validateFormState(){
	formState = document.getElementById("formState");
	if(!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}	
	return true;
}

function addState() {
	if(!validateFormState()) return;
	url = contextPath + "states/save";

	stateName = fieldStateName.val();
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = { name: stateName, country: { id: countryId, name: countryName } };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("The new state has been added. ");
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});
}



function selectNewlyAddedState(stateId, stateName) {
	optionalValue = stateId;
	$("<option>").val(optionalValue).text(stateName).appendTo(dropDownState);
	$("#dropDownState option[value='" + optionalValue + "']").prop("selected", true);

	fieldStateName.val("").focus();
}

function changedFormStateToNewAtState() {
	buttonAddState.val("Add");
	labelStateName.text("State Name:");

	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);

	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);

	labelStateName.text("Selected State: ")
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}


function loadStates() {
	selectedCountryId = dropDownCountry4States.val().split("-")[0];

	url = contextPath + "states/list_by_country/" + selectedCountryId;

	$.get(url, function(responseJSON) {
		dropDownState.empty();
		$.each(responseJSON, function(index, state) {
			optionalValue = state.id;
			$("<option>").val(optionalValue).text(state.name).appendTo(dropDownState);
		});

	}).done(function() {
		showToastMessage("All states have been loaded successfully.")
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});
}

function loadCountries4States() {

	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry4States.empty();
		$("<option>").val("0").text("--Please select one country --").appendTo(dropDownCountry4States);
		$.each(responseJSON, function(index, country) {
			optionalValue = country.id;  //+ "-" + country.code;
			$("<option>").val(optionalValue).text(country.name).appendTo(dropDownCountry4States);
		});

	}).done(function() {
		buttonLoadCountriesAtState.val("Refresh Country List")
		showToastMessage("All countries have been loaded successfully.")
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});

}
/*
function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

*/