package com.plainplanner.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.plainplanner.validation.PasswordMatches;
import com.plainplanner.validation.ValidPassword;
import com.plainplanner.validation.ValidUsername;

@PasswordMatches
public class UserDTO {
	@NotNull
	@NotEmpty
	@ValidUsername
	private String username;
	
	@NotNull
	@NotEmpty
	@ValidPassword
	private String password;
	private String confirmPassword;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
