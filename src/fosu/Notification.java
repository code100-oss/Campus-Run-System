package fosu;

public class Notification {
    private String id;
    private String recipient;
    private String title;
    private String content;
    private String type;
    private long createdAt;
    private boolean read;

    public Notification() {}

    public Notification(String id, String recipient, String title, String content, String type, long createdAt) {
        this.id = id;
        this.recipient = recipient;
        this.title = title;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
        this.read = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", recipient='" + recipient + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", read=" + read +
                '}';
    }
}
