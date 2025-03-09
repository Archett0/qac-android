package sg.edu.nus.qac_android.data.entity;

import java.util.UUID;

/**
 * @Author: Cooper
 * @Date: 3/9/2025
 * @Description:
 */
public class Comment {
    private UUID id;
    private String content;
    private String ownerId;
    private UUID answerId;

    public Comment(String content, String ownerId, UUID answerId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.ownerId = ownerId;
        this.answerId = answerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }
}
