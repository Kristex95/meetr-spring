package cybernetics.education.meetr_spring.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class WebMainController {

	@GetMapping("/main")
	public String mainPage(@ModelAttribute("username") String username, Model model) {
		// The username will automatically be available via the Model from the redirect
		model.addAttribute("username", username);
		return "main";  // This should map to the main page template (main.html)
	}
}