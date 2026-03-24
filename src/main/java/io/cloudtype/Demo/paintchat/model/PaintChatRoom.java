package io.cloudtype.Demo.paintchat.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paint_chat_room")
public class PaintChatRoom
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String roomId;

    @Column(nullable = false, unique = true, length = 120)
    private String roomName;

    @Column(nullable = false, length = 500)
    private String roomDescription;

    @Column(nullable = false)
    private boolean enterEnabled;

    @Column(nullable = false)
    private boolean drawEnabled;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PaintChatRoom()
    {
    }

    public PaintChatRoom(String roomId, String roomName, String roomDescription, boolean enterEnabled, boolean drawEnabled, LocalDateTime createdAt)
    {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.enterEnabled = enterEnabled;
        this.drawEnabled = drawEnabled;
        this.createdAt = createdAt;
    }

    public Long getId()
    {
        return id;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomDescription()
    {
        return roomDescription;
    }

    public boolean isEnterEnabled()
    {
        return enterEnabled;
    }

    public boolean isDrawEnabled()
    {
        return drawEnabled;
    }

    public void disableDrawing()
    {
        this.drawEnabled = false;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}
