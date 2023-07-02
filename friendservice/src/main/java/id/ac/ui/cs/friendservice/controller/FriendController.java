package id.ac.ui.cs.friendservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;
import id.ac.ui.cs.friendservice.service.FriendRequestService;
import id.ac.ui.cs.friendservice.service.FriendService;

@RestController
@RequestMapping(path = "/friend/api")
public class FriendController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendService friendService;

    private static final String FAILED = "Failed";

    @PostMapping(path = "/request", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<RequestApprovalDTO> requestFriend(@RequestBody RequestFriendDTO request){
        String sender = request.getSenderId();
        String receiver = request.getReceiverId();

        if (sender.equals(receiver)) {
            var requestApprovalDTO = new RequestApprovalDTO(FAILED, "Can't be friend with themselves");
            return ResponseEntity.ok(requestApprovalDTO);
        }

        if (friendRequestService.exist(sender, receiver)) {
            var requestApprovalDTO = new RequestApprovalDTO(FAILED, "Is already requesting");
            return ResponseEntity.ok(requestApprovalDTO);
        }

        if (friendService.exist(sender, receiver)) {
            var requestApprovalDTO = new RequestApprovalDTO(FAILED, "Is already a friend");
            return ResponseEntity.ok(requestApprovalDTO);
        }

        return ResponseEntity.ok(friendRequestService.request(request));
    }

    @PostMapping(path = "/request-remove", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<RequestApprovalDTO> removeFriendRequest(@RequestBody RequestFriendDTO request){
        if (!friendRequestService.exist(request.getSenderId(), request.getReceiverId())) {
            var requestApprovalDTO = new RequestApprovalDTO(FAILED, "Request doesn't exist");
            return ResponseEntity.ok(requestApprovalDTO);
        }

        return ResponseEntity.ok(friendRequestService.remove(request));
    }

    @PostMapping(path = "/friend-remove", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<RequestApprovalDTO> removeFriend(@RequestBody RequestFriendDTO request){
        if (!friendService.exist(request.getSenderId(), request.getReceiverId())) {
            var requestApprovalDTO = new RequestApprovalDTO(FAILED, "Friend doesn't exist");
            return ResponseEntity.ok(requestApprovalDTO);
        }
        return ResponseEntity.ok(friendService.removeOrdered(request));
    }

    @GetMapping(path="/outgoing-request", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<UserIdListDTO> listOutgoingFriendRequest(@RequestParam(value="userId") String user) {
        return ResponseEntity.ok(friendRequestService.getOutgoingFriendRequests(user));
    }

    @GetMapping(path="/incoming-request", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<UserIdListDTO> listIncomingFriendRequest(@RequestParam(value="userId") String user) {
        return ResponseEntity.ok(friendRequestService.getIncomingFriendRequests(user));
    }

    @GetMapping(path="/friend", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<UserIdListDTO> listFriend(@RequestParam(value="userId") String user) {
        return ResponseEntity.ok(friendService.getFriends(user));
    }

}
