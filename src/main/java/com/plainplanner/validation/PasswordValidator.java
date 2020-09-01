package com.plainplanner.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String PASSWORD_PATTERN = "^[A-Za-z\\d*.!@$%^&:;<>,.?~_+-=|]+$";
	private static final int MIN_LENGTH = 8;
	private static final int MAX_LENGTH = 50;

	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		return password != null && correctLength(password) && containsValidCharacters(password);
	}

	public boolean containsValidCharacters(String password) {
		matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	public boolean correctLength(String password) {
		return (password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH);
	}

}
