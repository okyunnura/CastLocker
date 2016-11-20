package net.okyunnura.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/download")
public class DownloadController {

	@RequestMapping(method = RequestMethod.GET)
	public String download(Model model) {
		return "download";
	}
}
