package io.cloudtype.Demo.paintchat.websocket;

public class PaintChatCursorMessage
{
    private String guestNickname;
    private double ratioX;
    private double ratioY;

    public String getGuestNickname()
    {
        return guestNickname;
    }

    public void setGuestNickname(String guestNickname)
    {
        this.guestNickname = guestNickname;
    }

    public double getRatioX()
    {
        return ratioX;
    }

    public void setRatioX(double ratioX)
    {
        this.ratioX = ratioX;
    }

    public double getRatioY()
    {
        return ratioY;
    }

    public void setRatioY(double ratioY)
    {
        this.ratioY = ratioY;
    }
}
