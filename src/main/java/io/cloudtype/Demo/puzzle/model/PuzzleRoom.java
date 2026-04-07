package io.cloudtype.Demo.puzzle.model;

public class PuzzleRoom
{
    private final String roomId;
    private final String puzzleTypeCode;
    private final String roomName;
    private final String roomDescription;
    private final int currentPlayers;
    private final int maxPlayers;
    private final String statusLabel;

    public PuzzleRoom(String roomId, String puzzleTypeCode, String roomName, String roomDescription, int currentPlayers, int maxPlayers,
        String statusLabel)
    {
        this.roomId = roomId;
        this.puzzleTypeCode = puzzleTypeCode;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.statusLabel = statusLabel;
    }

    public String getRoomId()
    {
        return roomId;
    }

    public String getPuzzleTypeCode()
    {
        return puzzleTypeCode;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomDescription()
    {
        return roomDescription;
    }

    public int getCurrentPlayers()
    {
        return currentPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public String getStatusLabel()
    {
        return statusLabel;
    }
}
