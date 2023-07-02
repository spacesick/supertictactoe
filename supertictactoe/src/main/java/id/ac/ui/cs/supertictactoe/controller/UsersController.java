package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import id.ac.ui.cs.supertictactoe.service.UserService;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public final String viewUsers(final Model model) {
        model.addAttribute("users", userService.getUserList());
        return "users";
    }
}
