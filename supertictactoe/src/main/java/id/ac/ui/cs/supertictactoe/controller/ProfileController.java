package id.ac.ui.cs.supertictactoe.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;
import id.ac.ui.cs.supertictactoe.service.GameService;
import id.ac.ui.cs.supertictactoe.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    private String redirect = "redirect:";

    @GetMapping("")
    public final String getUserProfilePage(final Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (User) auth.getPrincipal();

        model.addAttribute("user", authUser);

        return "profile";
    }

    @GetMapping("/edit")
    public final String getUserProfileEditorPage(final Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (User) auth.getPrincipal();

        model.addAttribute("user", authUser);
        model.addAttribute("profile", new UserProfileDTO(authUser.getFirstName(), authUser.getLastName()));

        return "edit_profile";
    }

    @PostMapping("/edit")
    public final String updateUserProfile(final Model model, final UserProfileDTO profile) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (User) auth.getPrincipal();

        userService.updateUser(authUser, profile);
        return redirect;
    }

    @GetMapping("/history")
    public final String getUserMatchHistory(final Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (User) auth.getPrincipal();

        var games = gameService.getGamesByPlayerUsername(authUser.getUsername());
        
        model.addAttribute("games", games);

        model.addAttribute("user", authUser);

        return "history";
    }
}
