package COP3530.Project3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("test", "test");
        logger.error("Working Directory = " + System.getProperty("user.dir"));
        return "index";
    }
}
