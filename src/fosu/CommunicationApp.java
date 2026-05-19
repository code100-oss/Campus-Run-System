package fosu;

import java.util.List;

public class CommunicationApp {
    public static void main(String[] args) {
        CommunicationService communicationService = new CommunicationService();

        String msg1 = communicationService.sendMessage("buyer01", "seller01", "prod-001", "您好，这件商品还能讲价吗？");
        String msg2 = communicationService.sendMessage("seller01", "buyer01", "prod-001", "您好，可以的，最低价180元。\n");
        communicationService.sendMessage("buyer02", "seller01", "prod-002", "请问商品何时可以发货？");

        System.out.println("== seller01 与 buyer01 的对话 ==");
        List<Message> conversation = communicationService.getConversation("seller01", "buyer01");
        conversation.forEach(System.out::println);

        System.out.println("\n== seller01 的通知列表 ==");
        List<Notification> notifications = communicationService.getNotificationsForUser("seller01");
        notifications.forEach(System.out::println);

        System.out.println("\n== 标记消息已读 ==");
        communicationService.markMessageRead(msg1);
        System.out.println(communicationService.getMessagesForUser("buyer01"));

        System.out.println("\n== 标记通知已读 ==");
        if (!notifications.isEmpty()) {
            communicationService.markNotificationRead(notifications.get(0).getId());
            System.out.println(communicationService.getNotificationsForUser("seller01"));
        }
    }
}
