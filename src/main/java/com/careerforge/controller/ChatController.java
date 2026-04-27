package com.careerforge.controller;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;      // ✅ REQUIRED
import java.util.HashMap;       // ✅ REQUIRED
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.careerforge.model.Message;
import com.careerforge.repository.MessageRepository;
import com.careerforge.security.OnlineUserService;
@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    private final MessageRepository repo;
    private final OnlineUserService onlineService;

    public ChatController(MessageRepository repo, OnlineUserService onlineService) {
        this.repo = repo;
        this.onlineService=onlineService;
    }

    // ✅ SEND MESSAGE
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message msg) {
        msg.setTime(LocalDateTime.now());
        return repo.save(msg);
    }

    // ✅ GET CHAT HISTORY
    @GetMapping("/history")
    public List<Message> getChat(
        @RequestParam String user1,
        @RequestParam String user2
    ) {
        return repo
            .findBySenderEmailAndReceiverEmailOrReceiverEmailAndSenderEmailOrderByTimeAsc(
                user1, user2,
                user1, user2
            );
    }
    @GetMapping("/conversations")
public List<String> getConversations(@RequestParam String email) {
    return repo.findDistinctUsers(email);
}
@GetMapping("/unread")
public Map<String, Long> getUnread(@RequestParam String email) {
    List<Object[]> data = repo.getUnreadCounts(email);

    Map<String, Long> result = new HashMap<>();

    for (Object[] row : data) {
        result.put((String) row[0], (Long) row[1]);
    }

    return result;
}
@GetMapping("/status")
public Map<String, Object> getStatus(@RequestParam String email) {
    Map<String, Object> res = new HashMap<>();
    res.put("online", onlineService.isOnline(email));
    res.put("lastSeen", onlineService.getLastSeenTime(email));
    return res;
}
@PostMapping("/upload")
public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

    Path path = Paths.get("uploads/" + fileName);
    Files.createDirectories(path.getParent());

    Files.write(path, file.getBytes());

    return "http://localhost:8081/uploads/" + fileName;
}

}
