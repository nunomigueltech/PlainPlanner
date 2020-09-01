/**
 * Handles registration form validation
 */

let usernameInput = document.getElementById('username');
usernameInput.onchange = function() {
	highlightInput('username', isUsernameValid());
};

let passwordInput = document.getElementById('password');
let confirmPasswordInput = document.getElementById('confirm-password');
passwordInput.onchange = validatePassword;
confirmPasswordInput.onchange = validatePassword;

let registerButton = document.getElementById('register-button');
registerButton.addEventListener('click', function(event) {
	if (!validatePassword()) {
		event.preventDefault();
	}
});

function isUsernameValid() {
	let usernameRegex = /^[\d|a-z|A-Z]+$/g;
	const hasRequiredLength = usernameInput.value.length > 0 && usernameInput.value.length <= 32;
	const hasAllowedCharacters = usernameRegex.test(usernameInput.value);
	let isUsernameValid = hasRequiredLength && hasAllowedCharacters;
	
	if (!isUsernameValid) {
		let usernameValidityMessage = document.getElementById('username-message');
		usernameValidityMessage.hidden = false;
		if (usernameInput.value.length === 0) {
			usernameValidityMessage.innerText = "Username is required.";
		} else if (!hasRequiredLength) {
			usernameValidityMessage.innerText = "Username must be between 1 and 32 characters long.";
		} else {
			usernameValidityMessage.innerText = "Incorrect username format.";
		}
	}
	
	return isUsernameValid;
}
	
function validatePasswordsMatch() {
	if (passwordInput.value === '' || confirmPasswordInput.value === '') return;
	
	const warningLabels = document.getElementsByClassName('password-match');
	const passwordsMatch = passwordInput.value === confirmPasswordInput.value; 
	
	return passwordsMatch;
};

function validatePasswordFormat(passwordField) {
	let uppercaseRegex = /[A-Z]/g;
	let lowercaseRegex = /[a-z]/g;
	let numericRegex = /[\d]/g;
	let allowedPasswords = /^[A-Za-z\d*.!@$%^&:;<>,.?~_+-=|]+$/g;
	
	const hasRequiredLength = passwordField.value.length >= 8 && passwordField.value.length <= 255;
	const hasRequiredCharacters = uppercaseRegex.test(passwordField.value) && lowercaseRegex.test(passwordField.value) && numericRegex.test(passwordField.value);
	const hasAllowedCharacters = allowedPasswords.test(passwordField.value);
	const requirementsMet = hasRequiredLength && hasRequiredCharacters && hasAllowedCharacters;

	return requirementsMet;
}

function validatePassword() {
	const passwordsMatch = validatePasswordsMatch();
	const isPasswordValid = validatePasswordFormat(passwordInput);
	const isConfirmPasswordValid = validatePasswordFormat(confirmPasswordInput);
	
	if (!isPasswordValid) {
		updatePasswordError(false, "Passwords must follow the requirements above.");
	} else if (!isConfirmPasswordValid) {
		updatePasswordError(false, "Passwords must follow the requirements above.");
	} else if (!passwordsMatch) {
		updatePasswordError(false, "Passwords must match.");
	} else {
		updatePasswordError(true, "");
	}
	highlightInput('password', isPasswordValid && passwordsMatch);
	highlightInput('confirm-password', isConfirmPasswordValid && passwordsMatch);
	
	return isPasswordValid && passwordsMatch;
}

function highlightInput(elementID, isValid) {
	let element = document.getElementById(elementID);
	if (isValid) {
		element.style.borderColor = 'green';
	} else {
		element.style.borderColor = 'red';
	}
}

function updatePasswordError(hideMessage, errorMessage) {
	const formatLabels = document.getElementsByClassName('password-error');
	for (let label of formatLabels) {
		label.hidden = hideMessage; // if requirements NOT met, then the label is visible
		label.innerText = errorMessage;
	}
}