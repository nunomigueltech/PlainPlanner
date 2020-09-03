package com.plainplanner.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plainplanner.dto.ItemDTO;
import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.TextNote;
import com.plainplanner.entities.User;
import com.plainplanner.services.BucketService;
import com.plainplanner.services.IdeaService;
import com.plainplanner.services.ProjectService;
import com.plainplanner.services.TextNoteService;
import com.plainplanner.services.UserService;

@Controller
public class IdeaController {

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
	
	@RequestMapping("/addNewItem")
	public ModelAndView addNewItem(@ModelAttribute("newItem") @Valid ItemDTO dto, Model model) {
		ModelAndView resultingPage = new ModelAndView("redirect:/dashboard");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			User currentUser = userService.getUserByUsername(auth.getName());
			List<Idea> upcomingTasks = userService.getUpcomingTasks(currentUser);
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
	public String updateIdea(@ModelAttribute("item") @Valid ItemDTO dto, Model model) {
		if (dto == null) return "/dashboard";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, dto.getId())) {
			Idea idea = ideaService.getIdea(dto.getId());

			ideaService.updateTitle(idea, dto.getTitle());
			ideaService.updateDescription(idea, dto.getContent());
			if (idea.getDeadline() != null) {
				ideaService.updateDeadline(idea, dto.getDate());
			}
			
			Bucket containingBucket = ideaService.getContainingBucket(idea);
			Bucket targetBucket = bucketService.getBucket(dto.getBucketID());
			bucketService.removeIdea(containingBucket, idea);
			bucketService.addIdea(targetBucket, idea);
			
			Project containingProject = ideaService.getContainingProject(idea);
			projectService.removeIdea(containingProject, idea);
			if (dto.getProjectID() != -1) {
				Project targetProject = projectService.getProject(dto.getProjectID());
				projectService.addIdea(targetProject, idea);
			}
			
			return "redirect:/idea/" + dto.getId();
		} else {
			return "/dashboard";
		}

	}
	
	@RequestMapping("/idea/{id}")
	public String idea(@PathVariable("id") Long id, Model model, @ModelAttribute("redirectURL") String redirectURL, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, id)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);

			Idea idea = ideaService.getIdea(id);
			model.addAttribute("idea", ideaService.getIdea(id));
			model.addAttribute("bucket", ideaService.getContainingBucket(idea));
			model.addAttribute("buckets", currentUser.getBuckets());
			model.addAttribute("project", ideaService.getContainingProject(idea));
			model.addAttribute("projects", currentUser.getProjects());
			model.addAttribute("redirectURL", redirectURL);
			return "idea";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
	
	@RequestMapping("/projectIdea/{projectId}/{ideaId}")
	public String projectIdea(@PathVariable("projectId") Long projectId, @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		redirectAttrs.addFlashAttribute("redirectURL", "/project/" + projectId);
		return "redirect:/idea/" + ideaId;
	}
	
	@RequestMapping("/bucketIdea/{bucketId}/{ideaId}")
	public String bucketIdea(@PathVariable("bucketId") Long bucketId, @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		redirectAttrs.addFlashAttribute("redirectURL", "/bucket/" + bucketId);
		return "redirect:/idea/" + ideaId;
	}
	
	@RequestMapping("/dashboardIdea/{ideaId}")
	public String projectIdea( @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		redirectAttrs.addFlashAttribute("redirectURL", "/dashboard");
		return "redirect:/idea/" + ideaId;
	}
	
	@RequestMapping("/complete/{id}")
	public String complete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, id)) {
			Idea idea = ideaService.getIdea(id);
			ideaService.completeIdea(idea);
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
		
		if (userService.hasIdea(currentUser, id)) {
			Idea idea = ideaService.getIdea(id);
			Bucket targetBucket = ideaService.getContainingBucket(idea);
			Project targetProject = ideaService.getContainingProject(idea);
			bucketService.removeIdea(targetBucket, idea);
			projectService.removeIdea(targetProject, idea);
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
		}
		return "redirect:/dashboard";
	}
}
