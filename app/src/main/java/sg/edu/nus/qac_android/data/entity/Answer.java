package sg.edu.nus.qac_android.data.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Answer {
    private UUID id;
    private UUID questionId;
    private String content;
    private LocalDateTime createdAt;
    private UUID ownerId;

    public Answer(UUID id, UUID questionId, String content, LocalDateTime createdAt, UUID ownerId) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
    }

    public UUID getId() { return id; }
    public UUID getQuestionId() { return questionId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UUID getOwnerId() { return ownerId; }
}
