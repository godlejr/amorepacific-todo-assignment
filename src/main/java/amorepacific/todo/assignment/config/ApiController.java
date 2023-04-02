package amorepacific.todo.assignment.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api")
public class ApiController {

    @GetMapping("/docs")
    public String api() {
        return "redirect:/swagger-ui.html";
    }
}
