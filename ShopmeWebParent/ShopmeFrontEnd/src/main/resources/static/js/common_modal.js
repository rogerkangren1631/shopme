
function showErrorDialog(message) {
	showModalDialog("Error", message);
}
function showWarningDialog(message) {
	showModalDialog("Warning", message);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
