package io.cloudtype.Demo.paintchat.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paint_chat_stroke")
public class PaintChatStroke
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String roomId;

    @Column(nullable = false, length = 100)
    private String guestNickname;

    @Column(nullable = false, length = 20)
    private String color;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private double fromRatioX;

    @Column(nullable = false)
    private double fromRatioY;

    @Column(nullable = false)
    private double toRatioX;

    @Column(nullable = false)
    private double toRatioY;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PaintChatStroke()
    {
    }

    public PaintChatStroke(
            String roomId,
            String guestNickname,
            String color,
            int size,
            double fromRatioX,
            double fromRatioY,
            double toRatioX,
            double toRatioY,
            LocalDateTime createdAt
    )
    {
        this.roomId = roomId;
        this.guestNickname = guestNickname;
        this.color = color;
        this.size = size;
        this.fromRatioX = fromRatioX;
        this.fromRatioY = fromRatioY;
        this.toRatioX = toRatioX;
        this.toRatioY = toRatioY;
        this.createdAt = createdAt;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public String getGuestNickname()
    {
        return guestNickname;
    }

    public String getColor()
    {
        return color;
    }

    public int getSize()
    {
        return size;
    }

    public double getFromRatioX()
    {
        return fromRatioX;
    }

    public double getFromRatioY()
    {
        return fromRatioY;
    }

    public double getToRatioX()
    {
        return toRatioX;
    }

    public double getToRatioY()
    {
        return toRatioY;
    }
}
