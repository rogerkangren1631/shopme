var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");

	buttonLoad.click(function() {
		loadCountries();
	});

	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});

	buttonAddCountry.click(function() {
		if (buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changedFormStateToNew();
		}
	});

	buttonUpdateCountry.click(function() {
		updateCountry();
	});

	buttonDeleteCountry.click(function() {
		deleteCountry();
	});
});

function deleteCountry() {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
	url = contextPath + "countries/delete/" + countryId;
		
		$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changedFormStateToNew();
		showToastMessage("The country has been deleted successfully.");
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});
}

function updateCountry() {
    if( !validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropDownCountry.val().split("-")[0];

	jsonData = { id: countryId, name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The new country has been updated. ");
		changedFormStateToNew();
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});

}

function validateFormCountry(){
	formCountry = document.getElementById("formCountry");
	if(!formCountry.checkValidity()) {
		formCountry.reportValidity();
		return false;
	}	
	return true;
}


function addCountry() {
    if( !validateFormCountry()) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryName, countryCode);
		showToastMessage("The new country has been added. ");
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ")
	});
}

function selectNewlyAddedCountry(countryId, countryName, countryCode) {
	optionalValue = countryId + "-" + countryCode;
	$("<option>").val(optionalValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountries option[value='" + optionalValue + "']").prop("selected", true);
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changedFormStateToNew() {
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");

	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);

	labelCountryName.text("Selected Country: ")
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);

	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries() {
      
	url = contextPath + "countries/list";

	$.get(url, function(responseJSON) {

		dropDownCountry.empty();
		$.each(responseJSON, function(index, country) {
			optionalValue = country.id + "-" + country.code;
			$("<option>").val(optionalValue).text(country.name).appendTo(dropDownCountry);
		});

	}).done(function() {
		buttonLoad.val("Refresh Country List");
		showToastMessage("All countries have been loaded successfully.");
	}).fail(function() {
		showToastMessage("ERROR: Cound not connect to server or server encountered an error. ");
	});

}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

