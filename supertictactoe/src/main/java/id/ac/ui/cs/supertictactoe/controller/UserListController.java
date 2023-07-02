package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/userlist")
public class UserListController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<User>> getListPost() {
        return ResponseEntity.ok(userService.getUserList());
    }
}
