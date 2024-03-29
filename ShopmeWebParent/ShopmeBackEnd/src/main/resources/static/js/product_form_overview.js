
dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {
	//	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	getCategoriesForNewForm();

});

function getCategoriesForNewForm(){
	catIdField = $("#cagegoryId");
	editMode = false;
	
	if(catIdField.length){
		editMode = true;
	}
	
	if(!editMode){
		getCategories();
	}
	
	
}


function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";
	//alert( url);
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});

	});
}

