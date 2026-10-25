package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ChatAPI")
public class ChatApiController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<?> handleGet(@RequestParam(value = "action", defaultValue = "getMessages") String action,
                                       @RequestParam(value = "clientId", required = false) Integer clientId,
                                       @RequestParam(value = "viewerRole", required = false, defaultValue = "client") String viewerRole,
                                       HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");

        switch (action) {
            case "getUsers":
                return ResponseEntity.ok(chatService.getChatUsers());

            case "getUnreadCount":
                if (user != null && user.getRoleId() != null && user.getRoleId() == 2) {
                    long unread = chatService.getUnreadCountForClient(user.getUserId());
                    return ResponseEntity.ok(Map.of("unread", unread));
                } else {
                    long unread = chatService.getUnreadCountForAdmin();
                    return ResponseEntity.ok(Map.of("unread", unread));
                }

            case "getMessages":
            default:
                Integer targetClientId = clientId;
                if (targetClientId == null && user != null) {
                    targetClientId = user.getUserId();
                }
                List<Map<String, Object>> msgs = chatService.getMessages(targetClientId, viewerRole);
                return ResponseEntity.ok(msgs);
        }
    }

    @PostMapping
    public ResponseEntity<?> handlePost(@RequestParam(value = "clientId", required = false) Integer clientId,
                                        @RequestParam(value = "clientEmail", required = false) String clientEmail,
                                        @RequestParam(value = "senderRole", defaultValue = "client") String senderRole,
                                        @RequestParam("messageText") String messageText,
                                        HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        Integer targetClientId = clientId;
        String targetEmail = clientEmail;

        if ("client".equalsIgnoreCase(senderRole) && user != null) {
            targetClientId = user.getUserId();
            targetEmail = user.getEmail();
        }

        chatService.saveMessage(targetClientId, targetEmail, senderRole, messageText);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Tin nhắn đã được lưu"));
    }
}
