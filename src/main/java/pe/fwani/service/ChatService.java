package pe.fwani.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.fwani.config.KafkaConstants;
import pe.fwani.model.ChatMessage;
import pe.fwani.model.ChatRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class ChatService {
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    public ChatRoom createRoom(String name){
        var room = new ChatRoom(name);
        chatRooms.put(room.getRoomId(), room);
        return room;
    }

    public ChatRoom changeRoomName(String roomId, String name) {
        var room = chatRooms.get(roomId);
        room.setName(name);
        return room;
    }

    public List<ChatRoom> findAllRooms() {
        return chatRooms.values().stream().toList();
    }
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public void sendMessage(String roomId, ChatMessage message) {
        try {
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, roomId, message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
