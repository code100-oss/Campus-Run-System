package fosu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CommunicationService {
    private final Map<String, Message> messages = new ConcurrentHashMap<>();
    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();

    public String sendMessage(String fromUser, String toUser, String productId, String content) {
        if (fromUser == null || fromUser.isEmpty() || toUser == null || toUser.isEmpty() || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("fromUser, toUser and content must not be empty");
        }
        String id = UUID.randomUUID().toString();
        Message message = new Message(id, fromUser, toUser, productId, content, System.currentTimeMillis());
        messages.put(id, message);
        createNotification(toUser, "新消息提醒", fromUser + " 给你发送了一条商品咨询消息。", "message");
        return id;
    }

    public List<Message> getMessagesForUser(String username) {
        if (username == null) return Collections.emptyList();
        return messages.values().stream()
                .filter(m -> username.equals(m.getFromUser()) || username.equals(m.getToUser()))
                .sorted((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<Message> getConversation(String userA, String userB) {
        if (userA == null || userB == null) return Collections.emptyList();
        return messages.values().stream()
                .filter(m -> (userA.equals(m.getFromUser()) && userB.equals(m.getToUser()))
                        || (userA.equals(m.getToUser()) && userB.equals(m.getFromUser())))
                .sorted((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()))
                .collect(Collectors.toList());
    }

    public boolean markMessageRead(String messageId) {
        Message message = messages.get(messageId);
        if (message == null) return false;
        message.setRead(true);
        return true;
    }

    public String createNotification(String recipient, String title, String content, String type) {
        if (recipient == null || recipient.isEmpty() || title == null || title.isEmpty() || content == null) {
            throw new IllegalArgumentException("notification fields must not be null");
        }
        String id = UUID.randomUUID().toString();
        Notification notification = new Notification(id, recipient, title, content, type, System.currentTimeMillis());
        notifications.put(id, notification);
        return id;
    }

    public List<Notification> getNotificationsForUser(String username) {
        if (username == null) return Collections.emptyList();
        return notifications.values().stream()
                .filter(n -> username.equals(n.getRecipient()))
                .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public boolean markNotificationRead(String notificationId) {
        Notification notification = notifications.get(notificationId);
        if (notification == null) return false;
        notification.setRead(true);
        return true;
    }

    public boolean removeNotification(String notificationId) {
        return notifications.remove(notificationId) != null;
    }
}
