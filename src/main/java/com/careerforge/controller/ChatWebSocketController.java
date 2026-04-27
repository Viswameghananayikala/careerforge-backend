package com.careerforge.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.careerforge.dto.ChatMessageDTO; // 🔥 ADD THIS IMPORT
import com.careerforge.model.Message;
import com.careerforge.repository.MessageRepository;
import com.careerforge.security.OnlineUserService;

@Controller
public class ChatWebSocketController {

    private final MessageRepository repo;
    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserService onlineService;


    public ChatWebSocketController(MessageRepository repo,
                                   SimpMessagingTemplate messagingTemplate,OnlineUserService onlineService) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
        this.onlineService=onlineService;
    }

    @MessageMapping("/chat")
    public void send(ChatMessageDTO dto) {

    System.out.println("DTO FILE URL: " + dto.fileUrl); // debug

    Message msg = new Message();

    msg.setSenderEmail(dto.senderEmail);
    msg.setReceiverEmail(dto.receiverEmail);

    // 🔥 IMPORTANT LOGIC
    if (dto.fileUrl != null && !dto.fileUrl.isEmpty()) {
        msg.setFileUrl(dto.fileUrl);
        msg.setFileType(dto.fileType);
        msg.setMessage(null); // file message
    } else {
        msg.setMessage(dto.message);
    }

    msg.setTime(LocalDateTime.now());
    msg.setSeen(false);
    msg.setDelivered(true);

    Message saved = repo.save(msg);

    messagingTemplate.convertAndSend(
        "/topic/chat/" + msg.getReceiverEmail(),
        saved
    );

    messagingTemplate.convertAndSend(
        "/topic/chat/" + msg.getSenderEmail(),
        saved
    );
    }
    
    @MessageMapping("/typing")
public void typing(Message msg) {
    messagingTemplate.convertAndSend(
        "/topic/typing/" + msg.getReceiverEmail(),
        msg
    );
} 
@MessageMapping("/seen")
public void seen(Message msg) {

    // ✅ UPDATE DB
    repo.markMessagesAsSeen(
        msg.getSenderEmail(),
        msg.getReceiverEmail()
    );

    // ✅ FETCH UPDATED MESSAGES
    List<Message> updatedMessages =
        repo.findBySenderEmailAndReceiverEmailOrReceiverEmailAndSenderEmailOrderByTimeAsc(
            msg.getSenderEmail(),
            msg.getReceiverEmail(),
            msg.getSenderEmail(),
            msg.getReceiverEmail()
        );

    // ✅ SEND FULL DATA
    messagingTemplate.convertAndSend(
        "/topic/seen/" + msg.getSenderEmail(),
        updatedMessages
    );
}
@MessageMapping("/online")
public void online(Message msg) {
    onlineService.userOnline(msg.getSenderEmail());

  messagingTemplate.convertAndSend(
    "/topic/status",
    msg.getSenderEmail() + ":ONLINE:" + System.currentTimeMillis()
);
}

@MessageMapping("/offline")
public void offline(Message msg) {
    onlineService.userOffline(msg.getSenderEmail());
long lastSeen = onlineService.getLastSeenTime(msg.getSenderEmail());

messagingTemplate.convertAndSend(
    "/topic/status",
    msg.getSenderEmail() + ":OFFLINE:" + lastSeen
);
}

}