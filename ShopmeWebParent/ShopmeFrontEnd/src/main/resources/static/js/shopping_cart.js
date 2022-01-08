decimalSeparator = decimalPointType == 'COMMA'? ',' :'.' ;
thousandsSeparator = thousandsPointType == 'COMMA'? ',' :'.' ;

$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));

	});

	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});

	$(".linkRemove").on("click", function(evt) {
		evt.preventDefault();
		removeProduct($(this));

	});
});

function decreaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val()) - 1;

	if (newQuantity > 0) {
		quantityInput.val(newQuantity);
		updateQuantity(productId, newQuantity);
	} else {
		showWarningDialog("Mininum quantity is 1 ");
	}
}

function increaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val()) + 1;

	if (newQuantity <= 5) {
		quantityInput.val(newQuantity)
		updateQuantity(productId, newQuantity);
	} else {
		showWarningDialog("Maxmum quantity is 5 ");
	}
}

function updateQuantity(productId, quantity) {
	url = contextPath + "cart/update/" + productId + "/" + quantity;

	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatedSubtotal) {

		updateSubtotal(updatedSubtotal, productId);
		updateTotal();
	}).fail(function() {

		showErrorDialog("Error while updating product quantity.")
	});
}

function updateSubtotal(updatedSubtoal, productId) {
	$("#subtotal" + productId).text(  formatCurrency(updatedSubtoal) );
}

function updateTotal() {
	total = 0.0;
	productCount = 0;
	$(".subtotal").each(function(index, element) {
		total += parseFloat( clearCurrencyFormat (element.innerHTML) );
		productCount++;
	});

	if (productCount < 1) {
		showEmptyShoppingCart();
	} else {
		$("#total").text( formatCurrency(total) );
	}
}

function showEmptyShoppingCart(){
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
}

function removeProduct(link) {
	url = link.attr("href");

	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		rowNumber = link.attr("rownumber");
		removeProductHTML(rowNumber);
		updateTotal();
		updateCountNumbers();

		showModalDialog("Shopping Cart", response);
	}).fail(function() {

		showErrorDialog("Error while removing product.")
	});
}

function removeProductHTML(rowNumber) {
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}

function updateCountNumbers() {
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);

	});
}

function formatCurrency(amount){
	
	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator ); 
}

function  clearCurrencyFormat(numberString){
	
	result = numberString.replaceAll(thousandsSeparator, "");
	return  result.replaceAll(decimalSeparator, ".");
}