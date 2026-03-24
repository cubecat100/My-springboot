package io.cloudtype.Demo.paintchat.websocket;

public class PaintChatDrawMessage
{
    private String guestNickname;
    private String color;
    private int size;
    private double fromRatioX;
    private double fromRatioY;
    private double toRatioX;
    private double toRatioY;

    public String getGuestNickname()
    {
        return guestNickname;
    }

    public void setGuestNickname(String guestNickname)
    {
        this.guestNickname = guestNickname;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public double getFromRatioX()
    {
        return fromRatioX;
    }

    public void setFromRatioX(double fromRatioX)
    {
        this.fromRatioX = fromRatioX;
    }

    public double getFromRatioY()
    {
        return fromRatioY;
    }

    public void setFromRatioY(double fromRatioY)
    {
        this.fromRatioY = fromRatioY;
    }

    public double getToRatioX()
    {
        return toRatioX;
    }

    public void setToRatioX(double toRatioX)
    {
        this.toRatioX = toRatioX;
    }

    public double getToRatioY()
    {
        return toRatioY;
    }

    public void setToRatioY(double toRatioY)
    {
        this.toRatioY = toRatioY;
    }
}
