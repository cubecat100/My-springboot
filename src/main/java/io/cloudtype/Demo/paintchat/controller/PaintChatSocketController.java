package io.cloudtype.Demo.paintchat.controller;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import io.cloudtype.Demo.paintchat.model.PaintChatRoom;
import io.cloudtype.Demo.paintchat.service.PaintChatPresenceService;
import io.cloudtype.Demo.paintchat.service.PaintChatRoomService;
import io.cloudtype.Demo.paintchat.service.PaintChatStrokeService;
import io.cloudtype.Demo.paintchat.websocket.PaintChatCursorMessage;
import io.cloudtype.Demo.paintchat.websocket.PaintChatDrawMessage;
import io.cloudtype.Demo.paintchat.websocket.PaintChatPresenceMessage;
import io.cloudtype.Demo.paintchat.websocket.PaintChatPresenceSnapshot;

@Controller
public class PaintChatSocketController
{
    private final SimpMessagingTemplate messagingTemplate;
    private final PaintChatStrokeService paintChatStrokeService;
    private final PaintChatRoomService paintChatRoomService;
    private final PaintChatPresenceService paintChatPresenceService;

    public PaintChatSocketController(
            SimpMessagingTemplate messagingTemplate,
            PaintChatStrokeService paintChatStrokeService,
            PaintChatRoomService paintChatRoomService,
            PaintChatPresenceService paintChatPresenceService
    )
    {
        this.messagingTemplate = messagingTemplate;
        this.paintChatStrokeService = paintChatStrokeService;
        this.paintChatRoomService = paintChatRoomService;
        this.paintChatPresenceService = paintChatPresenceService;
    }

    @MessageMapping("/paintchat/{roomId}/presence.join")
    public void join(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            PaintChatPresenceMessage message
    )
    {
        PaintChatPresenceSnapshot snapshot = paintChatPresenceService.join(roomId, message.getGuestNickname(), sessionId);
        messagingTemplate.convertAndSend(presenceTopic(roomId), snapshot);
    }

    @MessageMapping("/paintchat/{roomId}/presence.leave")
    public void leave(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            PaintChatPresenceMessage message
    )
    {
        PaintChatPresenceSnapshot snapshot = paintChatPresenceService.leave(roomId, message.getGuestNickname(), sessionId);
        messagingTemplate.convertAndSend(presenceTopic(roomId), snapshot);
    }

    @MessageMapping("/paintchat/{roomId}/draw.send")
    public void draw(@DestinationVariable String roomId, PaintChatDrawMessage message)
    {
        PaintChatRoom room = paintChatRoomService.findByRoomId(roomId);

        if (room == null || room.isDrawEnabled() == false)
        {
            return;
        }

        boolean saved = paintChatStrokeService.saveStroke(roomId, message);

        if (saved == false)
        {
            return;
        }

        messagingTemplate.convertAndSend(drawTopic(roomId), message);
    }

    @MessageMapping("/paintchat/{roomId}/cursor.move")
    public void cursor(@DestinationVariable String roomId, PaintChatCursorMessage message)
    {
        messagingTemplate.convertAndSend(cursorTopic(roomId), message);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event)
    {
        PaintChatPresenceSnapshot snapshot = paintChatPresenceService.disconnect(event.getSessionId());

        if (snapshot != null)
        {
            messagingTemplate.convertAndSend(presenceTopic(snapshot.getRoomId()), snapshot);
        }
    }

    private String drawTopic(String roomId)
    {
        return "/topic/paintchat/" + roomId + "/draw.public";
    }

    private String cursorTopic(String roomId)
    {
        return "/topic/paintchat/" + roomId + "/cursor.public";
    }

    private String presenceTopic(String roomId)
    {
        return "/topic/paintchat/" + roomId + "/presence.public";
    }
}
