package io.cloudtype.Demo.paintchat.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.cloudtype.Demo.paintchat.websocket.PaintChatPresenceSnapshot;

@Service
public class PaintChatPresenceService
{
    private final Map<String, Map<String, Integer>> roomPresence = new ConcurrentHashMap<String, Map<String, Integer>>();
    private final Map<String, SessionPresence> sessionPresence = new ConcurrentHashMap<String, SessionPresence>();

    public PaintChatPresenceSnapshot join(String roomId, String guestNickname, String sessionId)
    {
        if (guestNickname == null || guestNickname.trim().isEmpty())
        {
            return snapshot(roomId);
        }

        Map<String, Integer> members = roomPresence.computeIfAbsent(roomId, key -> new ConcurrentHashMap<String, Integer>());
        members.merge(guestNickname, 1, Integer::sum);
        sessionPresence.put(sessionId, new SessionPresence(roomId, guestNickname));

        return snapshot(roomId);
    }

    public PaintChatPresenceSnapshot leave(String roomId, String guestNickname, String sessionId)
    {
        removePresence(roomId, guestNickname, sessionId);
        return snapshot(roomId);
    }

    public PaintChatPresenceSnapshot disconnect(String sessionId)
    {
        SessionPresence presence = sessionPresence.remove(sessionId);

        if (presence == null)
        {
            return null;
        }

        decrementNicknameCount(presence.roomId, presence.guestNickname);
        return snapshot(presence.roomId);
    }

    public PaintChatPresenceSnapshot snapshot(String roomId)
    {
        Map<String, Integer> members = roomPresence.get(roomId);
        PaintChatPresenceSnapshot snapshot = new PaintChatPresenceSnapshot();
        snapshot.setRoomId(roomId);

        List<String> participants = new ArrayList<String>();

        if (members != null)
        {
            for (String nickname : new java.util.TreeSet<String>(members.keySet()))
            {
                participants.add(nickname);
            }
        }

        snapshot.setParticipants(participants);
        snapshot.setConnectedCount(participants.size());
        return snapshot;
    }

    private void removePresence(String roomId, String guestNickname, String sessionId)
    {
        sessionPresence.remove(sessionId);
        decrementNicknameCount(roomId, guestNickname);
    }

    private void decrementNicknameCount(String roomId, String guestNickname)
    {
        Map<String, Integer> members = roomPresence.get(roomId);

        if (members == null || guestNickname == null)
        {
            return;
        }

        Integer count = members.get(guestNickname);

        if (count == null)
        {
            return;
        }

        if (count <= 1)
        {
            members.remove(guestNickname);
        }
        else
        {
            members.put(guestNickname, count - 1);
        }

        if (members.isEmpty())
        {
            roomPresence.remove(roomId);
        }
    }

    private static class SessionPresence
    {
        private final String roomId;
        private final String guestNickname;

        private SessionPresence(String roomId, String guestNickname)
        {
            this.roomId = roomId;
            this.guestNickname = guestNickname;
        }
    }
}
