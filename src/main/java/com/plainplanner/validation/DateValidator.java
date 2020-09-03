package com.plainplanner.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.plainplanner.dto.ItemDTO;

public class DateValidator implements ConstraintValidator<ValidDate, ItemDTO>{
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

	@Override
	public boolean isValid(ItemDTO dto, ConstraintValidatorContext context) {
		if (dto.getItemType().equals("task") || dto.getItemType().equals("project")) {
			if (dto.getDate() == null) return false;
			
			pattern = Pattern.compile(DATE_PATTERN);
			matcher = pattern.matcher(dto.getDate().toString());
			return matcher.matches();
		}

		return true;
	}

}
