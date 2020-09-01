package com.plainplanner.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.plainplanner.dto.UserDTO;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDTO>{
	
	@Override
	public boolean isValid(UserDTO userDTO, ConstraintValidatorContext context) {
		return userDTO.getPassword().equals(userDTO.getConfirmPassword());
	}

}
