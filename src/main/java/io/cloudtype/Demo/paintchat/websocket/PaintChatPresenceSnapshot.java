package io.cloudtype.Demo.paintchat.websocket;

import java.util.List;

public class PaintChatPresenceSnapshot
{
    private String roomId;
    private List<String> participants;
    private int connectedCount;

    public String getRoomId()
    {
        return roomId;
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    public List<String> getParticipants()
    {
        return participants;
    }

    public void setParticipants(List<String> participants)
    {
        this.participants = participants;
    }

    public int getConnectedCount()
    {
        return connectedCount;
    }

    public void setConnectedCount(int connectedCount)
    {
        this.connectedCount = connectedCount;
    }
}
