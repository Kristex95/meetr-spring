package cybernetics.education.meetr_spring.controller.web;

import cybernetics.education.meetr_spring.model.User;
import cybernetics.education.meetr_spring.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebLoginController {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder; // Assuming you're using this for hashing

	public WebLoginController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";  // Return the login page view
	}

	@PostMapping("/api/login")
	public String loginUser(@RequestParam("email") String email,
							@RequestParam("password") String password,
							Model model) {
		User user = userService.findByEmail(email);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			// Add username to the model to show on the main page
			model.addAttribute("username", email);
			return "main";  // Redirect to the main page
		} else {
			model.addAttribute("error", "Invalid username or password.");
			return "login";  // Return to the login page on error
		}
	}
}
