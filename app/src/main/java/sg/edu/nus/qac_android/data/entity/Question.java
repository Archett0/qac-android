package sg.edu.nus.qac_android.data.entity;

import java.util.UUID;

/**
 * @Author: Cooper
 * @Date: 2/25/2025
 * @Description:
 */
public class Question {
    private UUID id;
    private String title;
    private String content;
    private UUID ownerId;

    public Question() {
    }

    public Question(UUID id, String title, String content, UUID ownerId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getShortContent() {
        if (content.length() > 120) {
            return content.substring(0, 120) + "...";
        }
        return content;
    }
}
