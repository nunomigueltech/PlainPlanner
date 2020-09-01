const usernameField = document.getElementById('username');
usernameField.onchange = function() {
	highlightField(usernameField, 'usernameError', 'Username is required.');
};
const passwordField = document.getElementById('password');
passwordField.onchange = function() {
	highlightField(passwordField, 'passwordError', 'Password is required.');
};

const siginButton = document.getElementById('signin-button');
siginButton.addEventListener('click', function(event) {
	highlightField(usernameField, 'usernameError', 'Username is required.');
	highlightField(passwordField, 'passwordError', 'Password is required.');
	if (!isFieldValid(usernameField) || !isFieldValid(passwordField)) {
		event.preventDefault();
	}
});

function highlightField(field, fieldErrorLabelName, message) {
	const usernameError = document.getElementById(fieldErrorLabelName);
	if (!isFieldValid(field)) {	
		usernameError.innerText = message;
		usernameError.hidden = false;
		field.style.borderColor = 'red';
	} else {
		usernameError.hidden = true;
		field.style.borderColor = 'green';
	}
}

function isFieldValid(field) {
	return field.value !== '';
}