package com.plainplanner.controller;

import java.text.DecimalFormat;
import java.util.Date;
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

import com.plainplanner.dto.ItemDTO;
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
			double completedTaskPercentage = ((double) completedTasks.size() / tasks.size()) * 100;
			model.addAttribute("completedTasks", new DecimalFormat("#.##").format(completedTaskPercentage));
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
		ItemDTO dto = new ItemDTO();
		model.addAttribute("newItem", dto);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		List<Idea> upcomingTasks = userService.getUpcomingTasks(currentUser);
		model.addAttribute("upcoming", upcomingTasks);
		
		return "dashboard";
	}
	
	@RequestMapping("/addNewItem/{referrer}/{referrerId}")
	public ModelAndView addNewItem(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @ModelAttribute("newItem") @Valid ItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			List<Idea> upcomingTasks = userService.getUpcomingTasks(currentUser);
			resultingPage.addObject("upcoming", upcomingTasks);
			
			if (dto.getTitle().isEmpty()) return resultingPage;
			
			switch (dto.getItemType()) {
				case "idea":
					Idea newIdea = new Idea(dto.getTitle(), dto.getItemType());
					if (dto.getContent() != null) {
						newIdea.setDescription(dto.getContent());
					}
					if (dto.getBucketID() != null) {
						Bucket bucket = bucketService.getBucket(dto.getBucketID());
						bucketService.addIdea(bucket, newIdea);
					} else {
						bucketService.addIdea(currentUser.getDefaultBucket(), newIdea);
					}
					if (dto.getProjectID() != null) {
						Project project = projectService.getProject(dto.getProjectID());
						projectService.addIdea(project, newIdea);
					}
					break;
				case "project":
					Project newProject = new Project(dto.getTitle());
					newProject.setDeadline(dto.getDate());
					userService.addProject(currentUser, newProject);
					break;
				case "task":
					Idea newTask = new Idea(dto.getTitle(), dto.getItemType());
					if (dto.getContent() != null) {
						newTask.setDescription(dto.getContent());
					}
					newTask.setDeadline(dto.getDate());
					if (dto.getBucketID() != null) {
						Bucket bucket = bucketService.getBucket(dto.getBucketID());
						bucketService.addIdea(bucket, newTask);
					} else {
						bucketService.addIdea(currentUser.getDefaultBucket(), newTask);
					}
					if (dto.getProjectID() != null) {
						Project project = projectService.getProject(dto.getProjectID());
						projectService.addIdea(project, newTask);
					}
					break;
				case "note":
					TextNote newNote = new TextNote(dto.getTitle());
					userService.addTextNote(currentUser, newNote);
					break;
			}
			resultingPage.addObject("added", dto.getItemType());
			
			if (referrer.equals("dashboard")) {
				resultingPage.setViewName("redirect:/dashboard");
			} else {
				resultingPage.setViewName("redirect:/" + referrer + "/" + referrerId);
			}
		} else {
			resultingPage.setViewName("redirect:/signin");
		}
		
		return resultingPage;
	}
	
	@RequestMapping("/addProject")
	public String addProject(Model model) {
		ItemDTO dto = new ItemDTO();
		model.addAttribute("item", dto);
		return "addProject";
	}
	
	@RequestMapping("/handleAddProject")
	public ModelAndView handleAddProject(@ModelAttribute("item") @Valid ItemDTO dto, Model model) {
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

	@RequestMapping("/project/{id}")
	public String project(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasProject(currentUser, id)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);
			
			Project project = projectService.getProject(id);
			model.addAttribute("project", project);
			model.addAttribute("notes", project.getNotes());
			model.addAttribute("ideas", project.getIdeas());
			model.addAttribute("redirectURL", request.getHeader("referer"));
			return "project";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that project.");
			return "redirect:/projects";
		}
	}
	
	@RequestMapping("/editProject/{id}")
	public String editProject(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasProject(currentUser, id)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);
			
			Project project = projectService.getProject(id);
			model.addAttribute("project", project);
			return "editProject";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/projects";
		}
	}
	
	@RequestMapping("/updateProject")
	public String updateProject(@ModelAttribute("item") @Valid ItemDTO dto, RedirectAttributes redirectAttrs, Model model) {
		if (dto == null) return "/dashboard";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasProject(currentUser, dto.getId())) {			
			Project project = projectService.getProject(dto.getId());
			projectService.updateTitle(project, dto.getTitle());
			projectService.updateDeadline(project, dto.getDate());

			return "redirect:/project/" + dto.getId();
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/projects";
		}
	}
	
	@RequestMapping("/deleteProject/{id}")
	public String deleteProject(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasProject(currentUser, id)) {
			Project project = projectService.getProject(id);
			userService.removeProject(currentUser, project);
			projectService.removeProject(project);
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that project.");
		}
		return "redirect:/projects";
	}
	
	@RequestMapping("/addBucket")
	public String addBucket(Model model) {
		ItemDTO dto = new ItemDTO();
		model.addAttribute("item", dto);
		return "addBucket";
	}
	
	@RequestMapping("/handleAddBucket")
	public ModelAndView handleAddBucket(@ModelAttribute("item") @Valid ItemDTO dto, Model model) {
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
	
	@RequestMapping("/bucket/{id}")
	public String bucket(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasBucket(currentUser, id)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);
			
			Bucket bucket = bucketService.getBucket(id);
			model.addAttribute("bucket", bucket);
			model.addAttribute("ideas", bucket.getIdeas());
			return "bucket";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that bucket.");
			return "redirect:/buckets";
		}
	}
	
	@RequestMapping("/editBucket/{id}")
	public String editBucket(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasBucket(currentUser, id) && bucketService.canEdit(id)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);
			
			Bucket bucket = bucketService.getBucket(id);
			model.addAttribute("bucket", bucket);
			return "editBucket";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that bucket.");
			return "redirect:/buckets";
		}
	}
	
	@RequestMapping("/updateBucket")
	public String updateBucket(@ModelAttribute("item") @Valid ItemDTO dto, RedirectAttributes redirectAttrs, Model model) {
		if (dto == null) return "/buckets";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasBucket(currentUser, dto.getId()) && bucketService.canEdit(dto.getId())) {			
			Bucket bucket = bucketService.getBucket(dto.getId());
			bucketService.updateName(bucket, dto.getTitle());
			bucketService.updateDescription(bucket, dto.getContent());

			return "redirect:/bucket/" + dto.getId();
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that bucket.");
			return "redirect:/buckets";
		}
	}
	
	@RequestMapping("/deleteBucket/{id}")
	public String deleteBucket(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasBucket(currentUser, id)) {
			Bucket bucket = bucketService.getBucket(id);
			userService.removeBucket(currentUser, bucket);
			bucketService.removeBucket(id);
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that bucket.");
		}
		return "redirect:/buckets";
	}
	
	
	@RequestMapping("/addNote")
	public String addNote(Model model) {
		ItemDTO dto = new ItemDTO();
		model.addAttribute("item", dto);
		return "addNote";
	}
	
	@RequestMapping("/handleAddNote")
	public ModelAndView handleAddNote(@ModelAttribute("item") @Valid ItemDTO dto, Model model) {
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
	
	@RequestMapping("/editNote/{referrer}/{referrerId}/{noteId}")
	public String editNote(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("noteId") Long noteId, Model model, @ModelAttribute("redirectURL") String redirectURL, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasNote(currentUser, noteId)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);

			Note note = textNoteService.getTextNote(noteId);
			model.addAttribute("note", note);
			model.addAttribute("project", textNoteService.getContainingProject((TextNote) note));
			model.addAttribute("projects", currentUser.getProjects());
			model.addAttribute("referrer", referrer);
			model.addAttribute("referrerId", referrerId);
			model.addAttribute("referralURL", referrer + "/" + referrerId);
			switch (referrer) {
				case "dashboard":
					model.addAttribute("redirectURL", "dashboard");
					break;
				case "project":
					model.addAttribute("redirectURL", "project/" + referrerId);
					break;
				case "notes":
					model.addAttribute("redirectURL", "notes");
					break;
				default:
					redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
					return "redirect:/notes";
			}
			return "editNote";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/notes";
		}
	}
	
	@RequestMapping("/updateNote/{referrer}/{referrerId}/{noteId}")
	public String updateNote(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("noteId") Long noteId, @ModelAttribute("item") @Valid ItemDTO dto, Model model) {
		if (dto == null) return "/notes";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasNote(currentUser, dto.getId())) {
			TextNote note = textNoteService.getTextNote(dto.getId());

			textNoteService.updateContent(note, dto.getTitle());
			
			Project containingProject = textNoteService.getContainingProject(note);
			projectService.removeNote(containingProject, note);
			if (dto.getProjectID() != -1) {
				Project targetProject = projectService.getProject(dto.getProjectID());
				projectService.addNote(targetProject, note);
			}
			
			return "redirect:/editNote/" + referrer + "/" + referrerId + "/" + noteId;
		} else {
			return "redirect:/notes";
		}

	}
}
