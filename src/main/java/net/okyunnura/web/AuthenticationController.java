package net.okyunnura.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController {

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String loginRedirect() {
		return "redirect:/startup";
	}

	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout() {
		return "logout";
	}

	@RequestMapping(path = "/logout", method = RequestMethod.POST)
	public String logoutRedirect() {
		return "redirect:/startup";
	}
}
