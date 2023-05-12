package pe.fwani.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import pe.fwani.model.ChatMessage;
import pe.fwani.config.KafkaConstants;
import pe.fwani.model.ChatRoom;
import pe.fwani.service.ChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class ChatController {
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;
    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ChatRoom createChatRoom(@RequestParam String roomName) {
        return chatService.createRoom(roomName);
    }
    @PutMapping("/chat")
    public ChatRoom changeRoomName(@RequestParam String roomId, @RequestParam String roomName) {
        return chatService.changeRoomName(roomId, roomName);
    }

    @GetMapping("/chat")
    public List<ChatRoom> getChatRoomList() {
        return chatService.findAllRooms();
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    @GetMapping("/api/messages/{groupId}")
    public void getMessages(@PathVariable String groupId) {

    }

    @PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody ChatMessage message) {
        log.info("/api/send" + message.toString());
        message.setTimestamp(LocalDateTime.now().toString());
        try {
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public ChatMessage broadcastGroupMessage(@Payload ChatMessage message) {
        return message;
    }
    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public ChatMessage addUser(@Payload ChatMessage message,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
