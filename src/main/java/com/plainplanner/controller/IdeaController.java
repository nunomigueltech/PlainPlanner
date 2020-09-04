package com.plainplanner.controller;

import java.util.Date;
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
	
	@RequestMapping("/updateIdea/{referrer}/{referrerId}/{ideaId}")
	public String updateIdea(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("ideaId") Long ideaId, @ModelAttribute("item") @Valid ItemDTO dto, Model model) {
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
			
			return "redirect:/idea/" + referrer + "/" + referrerId + "/" + ideaId;
		} else {
			return "redirect:/dashboard";
		}

	}
	
	@RequestMapping("/addIdea/{referrer}/{referrerId}")
	public String addIdea(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, Model model, @ModelAttribute("redirectURL") String redirectURL, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		ItemDTO dto = new ItemDTO();
		model.addAttribute("item", dto);
		model.addAttribute("referrer", referrer);
		model.addAttribute("referrerId", referrerId);
		return "addIdea";
	}
	
	@RequestMapping("/addTask/{referrer}/{referrerId}")
	public String addTask(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, Model model, @ModelAttribute("redirectURL") String redirectURL, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		ItemDTO dto = new ItemDTO();
		model.addAttribute("item", dto);
		model.addAttribute("referrer", referrer);
		model.addAttribute("referrerId", referrerId);
		return "addTask";
	}
	
	@RequestMapping("/idea/{referrer}/{referrerId}/{ideaId}")
	public String idea(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("ideaId") Long ideaId, Model model, @ModelAttribute("redirectURL") String redirectURL, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, ideaId)) {
			ItemDTO dto = new ItemDTO();
			model.addAttribute("item", dto);

			Idea idea = ideaService.getIdea(ideaId);
			model.addAttribute("idea", ideaService.getIdea(ideaId));
			model.addAttribute("bucket", ideaService.getContainingBucket(idea));
			model.addAttribute("buckets", currentUser.getBuckets());
			model.addAttribute("project", ideaService.getContainingProject(idea));
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
				case "bucket":
					model.addAttribute("redirectURL", "bucket/" + referrerId);
					break;
				default:
					redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
					return "redirect:/dashboard";
			}
			return "idea";
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
	
	@RequestMapping("/complete/{newReferrer}/{referrer}/{referrerId}/{ideaId}")
	public String complete(@PathVariable("newReferrer") String newReferrer, @PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, ideaId)) {
			Idea idea = ideaService.getIdea(ideaId);
			ideaService.completeIdea(idea);
			
			if (newReferrer.equals("dashboard")) {
				return "redirect:/dashboard";
			} else if (newReferrer.equals("idea")) {
				return "redirect:/idea/" + referrer + "/" + referrerId + "/" + ideaId;
			} else {
				return "redirect:/" + referrer + "/" + referrerId + "/" + ideaId;
			}
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
	
	@RequestMapping("/deleteIdea/{referrer}/{referrerId}/{ideaId}")
	public String deleteIdea(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, ideaId)) {
			Idea idea = ideaService.getIdea(ideaId);
			Bucket targetBucket = ideaService.getContainingBucket(idea);
			Project targetProject = ideaService.getContainingProject(idea);
			bucketService.removeIdea(targetBucket, idea);
			projectService.removeIdea(targetProject, idea);
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
		}
		if (referrer.equals("dashboard")) {
			return "redirect:/dashboard";
		} else {
			return "redirect:/" + referrer + "/" + referrerId;
		}
	}
	
	@RequestMapping("/addIdeaDeadline/{referrer}/{referrerId}/{ideaId}")
	public String addIdeaDeadline(@PathVariable("referrer") String referrer, @PathVariable("referrerId") Long referrerId, @PathVariable("ideaId") Long ideaId, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.getUserByUsername(auth.getName());
		
		if (userService.hasIdea(currentUser, ideaId)) {
			Idea idea = ideaService.getIdea(ideaId);
			ideaService.updateDeadline(idea, new Date());
			
			model.addAttribute("idea", idea);
			return "redirect:/idea/" + referrer + "/" + referrerId + "/" + ideaId;
		} else {
			redirectAttrs.addAttribute("error", "You don't have permission to access that item.");
			return "redirect:/dashboard";
		}
	}
}
