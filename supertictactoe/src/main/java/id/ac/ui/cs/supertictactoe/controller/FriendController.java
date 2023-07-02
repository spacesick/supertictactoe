package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.RequestFriendDTO;
import id.ac.ui.cs.supertictactoe.service.FriendService;

@Controller
@RequestMapping("/friend")
public class FriendController {

    private static final String FEEDBACK_ATTR = "feedback";
    private static final String REDIRECT_MAIN = "redirect:list";

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public final String addUser(final RedirectAttributes attributes, @RequestParam("friendUsername") final String friendUsername) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var feedback = friendService.requestFriendByUsername(current.getUserId(), friendUsername);
        attributes.addFlashAttribute(FEEDBACK_ATTR, feedback);

        return REDIRECT_MAIN;
    }

    @PostMapping("/accept")
    public final String acceptUser(final RedirectAttributes attributes, @RequestParam("friendId") final String friendId) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var feedback = friendService.requestFriendById(current.getUserId(), friendId);
        attributes.addFlashAttribute(FEEDBACK_ATTR, feedback);

        return REDIRECT_MAIN;
    }

    @PostMapping("/ignore-request")
    public final String ignoreRequest(final RedirectAttributes attributes, @RequestParam("friendId") final String friendId) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var feedback = friendService.removeFriendRequest(new RequestFriendDTO(friendId, current.getUserId()));
        attributes.addFlashAttribute(FEEDBACK_ATTR, feedback);

        return REDIRECT_MAIN;
    }


    @PostMapping("/remove-request")
    public final String removeRequest(final RedirectAttributes attributes, @RequestParam("friendId") final String friendId) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var feedback = friendService.removeFriendRequest(new RequestFriendDTO(current.getUserId(), friendId));
        attributes.addFlashAttribute(FEEDBACK_ATTR, feedback);

        return REDIRECT_MAIN;
    }

    @PostMapping("/remove-friend")
    public final String removeFriend(final RedirectAttributes attributes, @RequestParam("friendId") final String friendId) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var feedback = friendService.removeFriend(new RequestFriendDTO(current.getUserId(), friendId));
        attributes.addFlashAttribute(FEEDBACK_ATTR, feedback);

        return REDIRECT_MAIN;
    }

    @GetMapping("/list")
    public final String listFriend(final ModelMap model, @ModelAttribute("feedback") final String feedback) {
        User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var friends = friendService.listFriends(current.getUserId());
        var incoming = friendService.listIncomingFriendRequest(current.getUserId());
        var outgoing = friendService.listOutgoingFriendRequest(current.getUserId());

        model.addAttribute("current", current.getUserId());
        model.addAttribute("friends", friends);
        model.addAttribute("incoming", incoming);
        model.addAttribute("outgoing", outgoing);
        model.addAttribute(FEEDBACK_ATTR, feedback);

        return "friend";
    }
}
