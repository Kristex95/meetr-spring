package cybernetics.education.meetr_spring.controller.web;

import cybernetics.education.meetr_spring.dto.UserDto;
import cybernetics.education.meetr_spring.dto.request.RegistrationRequest;
import cybernetics.education.meetr_spring.model.User;
import cybernetics.education.meetr_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebRegistrationController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("registrationRequest", new RegistrationRequest());
		return "register"; // Return Thymeleaf registration page
	}

	@PostMapping("/api/register")
	public String registerUser(RegistrationRequest request, RedirectAttributes redirectAttributes) {
		UserDto user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
		redirectAttributes.addFlashAttribute("username", user.getUsername());
		// Redirect to the main page (use the appropriate mapping for your main page)
		return "redirect:/main";
	}
}