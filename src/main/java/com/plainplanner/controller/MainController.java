package com.plainplanner.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.plainplanner.dto.NewItemDTO;
import com.plainplanner.dto.UserDTO;
import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Note;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.TextNote;
import com.plainplanner.entities.User;
import com.plainplanner.services.BucketService;
import com.plainplanner.services.IdeaService;
import com.plainplanner.services.ProjectService;
import com.plainplanner.services.TextNoteService;
import com.plainplanner.services.UserService;

@Controller
public class MainController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BucketService bucketService;
	
	@Autowired
	private TextNoteService textNoteService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IdeaService ideaService;
	
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
	public String projects(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			List<Project> projects = currentUser.getProjects();
			model.addAttribute("projects", projects);
		}
		
		return "projects";
	}
	
	@RequestMapping("/buckets")
	public String buckets(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			List<Bucket> buckets = currentUser.getBuckets();
			model.addAttribute("buckets", buckets);
		}
		
		return "buckets";
	}
	
	@RequestMapping("/notes")
	public String notes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			List<Note> notes = currentUser.getNotes();
			model.addAttribute("notes", notes);
		}
		
		return "notes";
	}
	
	@RequestMapping("/profile")
	public String handleProfile() {
		return "profile";
	}
	
	@RequestMapping("/statistics")
	public String statistics(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		List<Idea> ideas = currentUser.getBuckets().stream()
								.flatMap(bucket -> bucket.getIdeas().stream())
								.collect(Collectors.toList());
		
		List<Idea> tasks = ideas.stream()
							.filter(idea -> idea.getType().contentEquals("task"))
							.collect(Collectors.toList());
		
		List<Idea> completedTasks = ideas.stream()
				.filter(idea -> idea.isComplete())
				.collect(Collectors.toList());
		
		List<Bucket> buckets = currentUser.getBuckets();
		List<Project> projects = currentUser.getProjects();
		
		model.addAttribute("accountCreation", currentUser.getDateCreated());
		model.addAttribute("ideas", ideas.size());
		model.addAttribute("tasks", tasks.size());
		if (tasks.size() > 0) {
			model.addAttribute("completedTasks", completedTasks.size() / tasks.size());
		}
		model.addAttribute("buckets", buckets.size());
		model.addAttribute("projects", projects.size());
		return "statistics";
	}
	
	@RequestMapping("/settings")
	public String handleSettings(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		Map<String, Boolean> settings = currentUser.getSettings();
		model.addAttribute("settings", settings.entrySet());
		
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
	
	@GetMapping("/expiredSignin")
	public String expiredSignin(Model model) {
		model.addAttribute("error", "Your account is signed in from another location. Please sign in again.");
		return "signin";
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
			
			if (userService.addUser(newUser) != null) {
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
	public String handleDashboard(Model model) {
		NewItemDTO dto = new NewItemDTO();
		model.addAttribute("newItem", dto);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		List<Idea> upcomingTasks = ideaService.getUpcomingTasks(currentUser);
		model.addAttribute("upcoming", upcomingTasks);
		
		return "dashboard";
	}
	
	@RequestMapping("/addNewItem")
	public ModelAndView addNewItem(@ModelAttribute("newItem") @Valid NewItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView("redirect:/dashboard");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			List<Idea> upcomingTasks = ideaService.getUpcomingTasks(currentUser);
			resultingPage.addObject("upcoming", upcomingTasks);
			
			if (dto.getTitle().isEmpty()) return resultingPage;
			
			switch (dto.getItemType()) {
				case "idea":
					Idea newIdea = new Idea(dto.getTitle(), dto.getItemType());
					bucketService.addIdea(currentUser.getDefaultBucket(), newIdea);
					break;
				case "project":
					Project newProject = new Project(dto.getTitle());
					newProject.setDeadline(dto.getDate());
					userService.addProject(currentUser, newProject);
					break;
				case "task":
					Idea newTask = new Idea(dto.getTitle(), dto.getItemType());
					newTask.setDeadline(dto.getDate());
					bucketService.addIdea(currentUser.getDefaultBucket(), newTask);
					break;
				case "note":
					TextNote newNote = new TextNote(dto.getTitle());
					userService.addTextNote(currentUser, newNote);
					break;
			}
			resultingPage.addObject("added", dto.getItemType());
		} else {
			resultingPage.setViewName("redirect:/signin");
		}
		
		return resultingPage;
	}
	
	@RequestMapping("/updateIdea")
	public String updateIdea(@ModelAttribute("item") @Valid NewItemDTO dto, Model model) {
		if (dto == null) return "/dashboard";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		Idea idea = ideaService.getIdea(dto.getId());

		ideaService.updateTitle(idea, dto.getTitle());
		ideaService.updateDescription(idea, dto.getContent());
		ideaService.updateDeadline(idea, dto.getDate());
		
		Bucket containingBucket = ideaService.getContainingBucket(idea);
		Bucket targetBucket = bucketService.getBucket(dto.getBucketID());
		bucketService.removeIdea(containingBucket, idea);
		bucketService.addIdea(targetBucket, idea);
		
		if (dto.getProjectID() != -1) {
			Project containingProject = ideaService.getContainingProject(idea);
			Project targetProject = projectService.getProject(dto.getProjectID());
			projectService.removeIdea(containingProject, idea);
			projectService.addIdea(targetProject, idea);
		}
		
		return "redirect:/idea/" + dto.getId();
	}
	
	@RequestMapping("/idea/{id}")
	public String idea(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		Idea targetIdea = ideaService.getIdea(id);
		List<Idea> userIdeas = ideaService.getUserIdeas(currentUser);
		if (userIdeas != null && userIdeas.contains(targetIdea)) {
			NewItemDTO dto = new NewItemDTO();
			model.addAttribute("item", dto);
			model.addAttribute("idea", ideaService.getIdea(id));
			model.addAttribute("bucket", ideaService.getContainingBucket(targetIdea));
			model.addAttribute("buckets", currentUser.getBuckets());
			model.addAttribute("project", ideaService.getContainingProject(targetIdea));
			model.addAttribute("projects", currentUser.getProjects());
			model.addAttribute("redirectURL", request.getHeader("referer"));
			return "idea";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
	
	@RequestMapping("/complete/{id}")
	public String complete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		Idea targetIdea = ideaService.getIdea(id);
		List<Idea> userIdeas = ideaService.getUserIdeas(currentUser);
		if (userIdeas.contains(targetIdea)) {
			ideaService.completeIdea(targetIdea);
			return "redirect:" + request.getHeader("referer");
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
	
	@RequestMapping("/deleteIdea/{id}")
	public String deleteIdea(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		Idea targetIdea = ideaService.getIdea(id);
		List<Idea> userIdeas = ideaService.getUserIdeas(currentUser);
		if (userIdeas.contains(targetIdea)) {
			Bucket targetBucket = ideaService.getContainingBucket(targetIdea);
			Project targetProject = ideaService.getContainingProject(targetIdea);
			bucketService.removeIdea(targetBucket, targetIdea);
			projectService.removeIdea(targetProject, targetIdea);
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
		}
		return "redirect:/dashboard";
	}
	
	@RequestMapping("/addProject")
	public String addProject(Model model) {
		NewItemDTO dto = new NewItemDTO();
		model.addAttribute("item", dto);
		return "addProject";
	}
	
	@RequestMapping("/handleAddProject")
	public ModelAndView handleAddProject(@ModelAttribute("item") @Valid NewItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView("redirect:/projects");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			if (dto.getTitle().isEmpty()) return resultingPage;
			
			Project newProject = new Project(dto.getTitle());
			newProject.setDeadline(dto.getDate());
			userService.addProject(currentUser, newProject);
			resultingPage.addObject("success", "Successfully created project '" + dto.getTitle() + "'!");
		} else {
			resultingPage.setViewName("redirect:/signin");
		}
		
		return resultingPage;
	}

	
	@RequestMapping("/addBucket")
	public String addBucket(Model model) {
		NewItemDTO dto = new NewItemDTO();
		model.addAttribute("item", dto);
		return "addBucket";
	}
	
	@RequestMapping("/handleAddBucket")
	public ModelAndView handleAddBucket(@ModelAttribute("item") @Valid NewItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView("redirect:/buckets");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			if (dto == null || dto.getTitle().isEmpty()) return resultingPage;
			
			Bucket newBucket = new Bucket(dto.getTitle());
			newBucket.setDescription(dto.getContent());
			userService.addBucket(currentUser, newBucket);
			resultingPage.addObject("success", "Successfully created bucket '" + dto.getTitle() + "'!");
		} else {
			resultingPage.setViewName("redirect:/signin");
		}
		
		return resultingPage;
	}
	
	@RequestMapping("/addNote")
	public String addNote(Model model) {
		NewItemDTO dto = new NewItemDTO();
		model.addAttribute("item", dto);
		return "addNote";
	}
	
	@RequestMapping("/handleAddNote")
	public ModelAndView handleAddNote(@ModelAttribute("item") @Valid NewItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView("redirect:/notes");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			
			if (dto == null || dto.getTitle().isEmpty()) return resultingPage;
			
			TextNote newNote = new TextNote(dto.getTitle());
			userService.addTextNote(currentUser, newNote);
			resultingPage.addObject("success", "Successfully added new note!");
		} else {
			resultingPage.setViewName("redirect:/signin");
		}
		
		return resultingPage;
	}
}
