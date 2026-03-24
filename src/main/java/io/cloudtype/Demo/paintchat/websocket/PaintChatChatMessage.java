package io.cloudtype.Demo.paintchat.websocket;

public class PaintChatChatMessage
{
    private String guestNickname;
    private String content;
    private String createdAt;

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

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }
}
