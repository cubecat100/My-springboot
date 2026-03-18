package io.cloudtype.Demo.paintchat.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "guest_memo")
public class GuestMemo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String guestNickname;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId()
    {
        return id;
    }

    public String getGuestNickname()
    {
        return guestNickname;
    }

    public void setGuestNickname(String guestNickname)
    {
        this.guestNickname = guestNickname;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
