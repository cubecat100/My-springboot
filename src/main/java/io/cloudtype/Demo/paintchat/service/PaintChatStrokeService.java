package io.cloudtype.Demo.paintchat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.cloudtype.Demo.paintchat.domain.PaintChatStroke;
import io.cloudtype.Demo.paintchat.repository.PaintChatStrokeRepository;
import io.cloudtype.Demo.paintchat.websocket.PaintChatDrawMessage;

@Service
public class PaintChatStrokeService
{
    private static final long MAX_STROKES_PER_ROOM = 5000L;

    private final PaintChatStrokeRepository paintChatStrokeRepository;
    private final PaintChatRoomService paintChatRoomService;

    public PaintChatStrokeService(PaintChatStrokeRepository paintChatStrokeRepository, PaintChatRoomService paintChatRoomService)
    {
        this.paintChatStrokeRepository = paintChatStrokeRepository;
        this.paintChatRoomService = paintChatRoomService;
    }

    @Transactional(readOnly = true)
    public List<PaintChatDrawMessage> findDrawHistory(String roomId)
    {
        List<PaintChatStroke> strokes = paintChatStrokeRepository.findByRoomIdOrderByIdAsc(roomId);
        List<PaintChatDrawMessage> history = new ArrayList<PaintChatDrawMessage>();

        for (PaintChatStroke stroke : strokes)
        {
            PaintChatDrawMessage message = new PaintChatDrawMessage();
            message.setGuestNickname(stroke.getGuestNickname());
            message.setColor(stroke.getColor());
            message.setSize(stroke.getSize());
            message.setFromRatioX(stroke.getFromRatioX());
            message.setFromRatioY(stroke.getFromRatioY());
            message.setToRatioX(stroke.getToRatioX());
            message.setToRatioY(stroke.getToRatioY());
            history.add(message);
        }

        return history;
    }

    @Transactional
    public boolean saveStroke(String roomId, PaintChatDrawMessage message)
    {
        long currentCount = paintChatStrokeRepository.countByRoomId(roomId);

        if (currentCount >= MAX_STROKES_PER_ROOM)
        {
            paintChatRoomService.disableDrawing(roomId);
            return false;
        }

        PaintChatStroke stroke = new PaintChatStroke(
                roomId,
                message.getGuestNickname(),
                message.getColor(),
                message.getSize(),
                message.getFromRatioX(),
                message.getFromRatioY(),
                message.getToRatioX(),
                message.getToRatioY(),
                LocalDateTime.now()
        );

        paintChatStrokeRepository.save(stroke);

        if ((currentCount + 1L) >= MAX_STROKES_PER_ROOM)
        {
            paintChatRoomService.disableDrawing(roomId);
        }

        return true;
    }

    public long getMaxStrokesPerRoom()
    {
        return MAX_STROKES_PER_ROOM;
    }
}
