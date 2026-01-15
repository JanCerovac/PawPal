package root.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimeNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public RealtimeNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void pushToUser(String username, Object payload) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", payload);
    }

    public void broadcast(Object payload) {
        messagingTemplate.convertAndSend("/topic/ping", payload);
    }
}
