package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import id.ac.ui.cs.supertictactoe.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    public final String homepage(final Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) auth.getPrincipal();
        model.addAttribute("username", user.getUsername());
        return "homepage";
    }
}
