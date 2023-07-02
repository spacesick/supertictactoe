package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomsController {

    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public final String list(final Model model){

        model.addAttribute("rooms", roomService.getRoomList());
        return "rooms";
    }

    @GetMapping("/create")
    public final String create(final Model model){
        model.addAttribute("roomName", roomService.createRoom());
        return "create_room";
    }

    @PostMapping("/create")
    public final String create(@RequestParam final String roomName){

        SecurityContextHolder.getContext().getAuthentication();
        var room = roomService.createRoom(roomName);

        return "redirect:/rooms/join/" + room.getRoomId();
    }

    @GetMapping("/join/{roomId}")
    public final String join(@PathVariable final String roomId, final Model model) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) auth.getPrincipal();
        var room = roomService.joinRoom(user.getUserId(), roomId);

        model.addAttribute("room", room);
        model.addAttribute("userId", user.getUserId());
        return "join_room";
    }

}
