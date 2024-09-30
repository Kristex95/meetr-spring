package cybernetics.education.meetr_spring.controller;

import cybernetics.education.meetr_spring.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}