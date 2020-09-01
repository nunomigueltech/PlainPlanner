package com.plainplanner.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.plainplanner.dto.UserDTO;
import com.plainplanner.entities.User;
import com.plainplanner.services.UserService;

@Controller
public class MainController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String handleIndex() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "forward:dashboard";
		} else {
			return "index";
		}
	}
	
	// map "index" page back to the root, so it appears as PlainPlanner/ instead of PlainPlanner/index
	@RequestMapping("index")
	public RedirectView redirectIndex(HttpServletRequest request) {
		return new RedirectView(request.getContextPath());
	}
	
	@RequestMapping("/projects")
	public String handleProjects() {
		return "projects";
	}
	
	@RequestMapping("/buckets")
	public String handleBuckets() {
		return "buckets";
	}
	
	@RequestMapping("/notes")
	public String handleNotes() {
		return "notes";
	}
	
	@RequestMapping("/profile")
	public String handleProfile() {
		return "profile";
	}
	
	@RequestMapping("/statistics")
	public String handleStatistics() {
		return "statistics";
	}
	
	@RequestMapping("/settings")
	public String handleSettings() {
		return "settings";
	}
	
	@RequestMapping("/about")
	public String handleAbout() {
		return "about";
	}
	
	@RequestMapping("/signin")
	public String handleSignin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "forward:dashboard";
		} else {
			return "signin";
		}
	}
	
	@GetMapping("/register")
	public String handleRegisterLoad(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "forward:dashboard";
		} else {
			UserDTO userDTO = new UserDTO();
			model.addAttribute("user", userDTO);
			
			return "register";
		}
	} 
	
	@PostMapping("/register")
	public ModelAndView handleRegisterPost(@ModelAttribute("user") @Valid UserDTO userDTO) {
		ModelAndView resultingPage = new ModelAndView();
		
		if (userService.userExists(userDTO.getUsername())) {
			resultingPage.setViewName("register");
			resultingPage.addObject("usernameError", "The username '" + userDTO.getUsername() + "' already exists.");
		} else {
			String username = userDTO.getUsername().toLowerCase();
			String passwordHash = passwordEncoder.encode(userDTO.getPassword());
			User newUser = new User(username, passwordHash);
			
			if (userService.addUser(newUser)) {
				resultingPage.setViewName("signin");
				resultingPage.addObject("message", "Your account has been successfully registered. You may now sign in.");
			} else {
				resultingPage.setViewName("register");
				resultingPage.addObject("error", "The server has encountered an error and failed to register your account.");
			}			
		}
		
		return resultingPage;
	}
	
	@RequestMapping("/dashboard")
	public String handleDashboard() {
		return "dashboard";
	}
}
