var iconNames = {
	'PICKED':'fa-people-carry',
	'SHIPPING':'fa-shipping-fast', 
	'DELIVERED' :'fa-box-open', 
	'RETURNED': 'fa-undo'	
}

var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;
$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModel(link);
	});

	addEventHandleeForYesButton();
});

function sendRequestToUpdateOrderStatus(button){

    requestUrl= button.attr("href");
    //alert( "requestUrl is "+ requestUrl );
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showMessageModal("Order updated successfully.");
		updateStatusIconColor(response.orderId, response.status);
      console.log(response) ; 
	}).fail(function(err) {
		showMessageModal("Error updating order status.");
		 console.log(err) ;
	});
	
}

function updateStatusIconColor(orderId, status){
	link=$("#link"+status+orderId);
	link.replaceWith("<i class='fas " +iconNames[status]+" fa-2x icon-green'></i>"); 
}


function addEventHandleeForYesButton() {
	yesButton.click(function(e) {
		e.preventDefault();
	sendRequestToUpdateOrderStatus($(this));
	});
}


function showUpdateConfirmModel(link) {
	noButton.text("NO");
	yesButton.show();
	
	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href", link.attr("href"));

	confirmText.text("Are you sure you want to update status of the order ID #" + orderId
		+ " to " + status + "? ");
	confirmModalDialog.modal();
}

function showMessageModal(message){
	noButton.text("Close");
	yesButton.hide();
	confirmText.text(message);
}

