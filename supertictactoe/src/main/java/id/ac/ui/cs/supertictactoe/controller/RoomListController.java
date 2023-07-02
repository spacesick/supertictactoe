package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/roomlist")
public class RoomListController {

    @Autowired
    private RoomService roomService;

    @GetMapping(produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Room>> getListPost() {
        return ResponseEntity.ok(roomService.getRoomList());
    }
}
