package com.plainplanner.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String>{
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String USERNAME_PATTERN = "[\\d|a-z|A-Z]+";
	private static final int MIN_LENGTH = 1;
	private static final int MAX_LENGTH = 32;
	
	@Override
	public void initialize(ValidUsername constraintAnnotation) {
		pattern = Pattern.compile(USERNAME_PATTERN);
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return username != null && correctLength(username) && containsValidCharacters(username);
	}

	public boolean containsValidCharacters(String username) {
		matcher = pattern.matcher(username);
		return matcher.matches();
	}
	
	public boolean correctLength(String username) {
		return (username.length() >= MIN_LENGTH && username.length() <= MAX_LENGTH);
	}
}
