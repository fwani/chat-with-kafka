package pe.fwani.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import pe.fwani.service.ChatService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class ChatRoom {
    private final String roomId;
    private String name;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public ChatRoom(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + " 님이 입장했습니다.");
        }
        chatService.sendMessage(roomId, chatMessage);
    }
}
