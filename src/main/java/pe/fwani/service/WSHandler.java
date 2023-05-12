package pe.fwani.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pe.fwani.model.ChatMessage;

@Slf4j
@Component
public class WSHandler extends TextWebSocketHandler {
    @Autowired
    private ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = message.getPayload();
        log.info("payload : " + payload);
        var chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        var room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}
