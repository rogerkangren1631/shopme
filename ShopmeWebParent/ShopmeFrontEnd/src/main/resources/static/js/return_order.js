var returnModal;
var modalTitle;
var fieldNote;
var orderId;

$(document).ready(function(){ 
	returnModel = $("#returnOrderModal");
	modalTitle = $("#returnOrderModalTitle");
	fieldNote = $("#returnNote");
	
	$(".linkReturnOrder").on("click", function(e) {
		e.preventDefault();
		handleReturnOrderLink($(this));
	});
	
});

function submitReturnOrderForm(){
	reason =$("input[name='returnReason']:checked").val();
	note = fieldNote.val();
	sendReturnOrderRequest(reason, note);

}

function sendReturnOrderRequest(reason, note){
	requestUrl = contextPath + "order/return/"; 
	//alert(requestUrl); 
	requestBody ={orderId:orderId, reason:reason, note:note};
	
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody), 
		contentType:"application/json"
	}).done(function(returnSponse) {
	    console.log(returnSponse);
	}).fail(function(err) {
	    console.log(err);
	});
	
	
}

function handleReturnOrderLink(link){
	orderId = link.attr("orderId");
	modalTitle.text("Return Order ID #" + orderId);
	returnModel.modal("show");
	
}
