package io.cloudtype.Demo.paintchat.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.cloudtype.Demo.paintchat.model.PaintChatRoom;
import io.cloudtype.Demo.paintchat.repository.PaintChatRoomRepository;

@Service
public class PaintChatRoomService
{
    private static final long DAILY_ROOM_CREATION_LIMIT = 3L;

    private final PaintChatRoomRepository paintChatRoomRepository;

    public PaintChatRoomService(PaintChatRoomRepository paintChatRoomRepository)
    {
        this.paintChatRoomRepository = paintChatRoomRepository;
    }

    @Transactional(readOnly = true)
    public List<PaintChatRoom> findAll()
    {
        return paintChatRoomRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public PaintChatRoom findByRoomId(String roomId)
    {
        return paintChatRoomRepository.findByRoomId(roomId);
    }

    @Transactional
    public PaintChatRoom createRoom()
    {
        if (hasReachedDailyCreationLimit())
        {
            return null;
        }

        String roomId = createUniqueRoomId();
        String nextRoomName = createNextRoomName();
        PaintChatRoom room = new PaintChatRoom(roomId, nextRoomName, "", true, true, LocalDateTime.now());

        return paintChatRoomRepository.save(room);
    }

    @Transactional
    public void disableDrawing(String roomId)
    {
        PaintChatRoom room = paintChatRoomRepository.findByRoomId(roomId);

        if (room == null || room.isDrawEnabled() == false)
        {
            return;
        }

        room.disableDrawing();
        paintChatRoomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public boolean hasReachedDailyCreationLimit()
    {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return paintChatRoomRepository.countByCreatedAtBetween(start, end) >= DAILY_ROOM_CREATION_LIMIT;
    }

    public long getDailyRoomCreationLimit()
    {
        return DAILY_ROOM_CREATION_LIMIT;
    }

    private String createUniqueRoomId()
    {
        String roomId = UUID.randomUUID().toString();

        while (paintChatRoomRepository.existsByRoomId(roomId))
        {
            roomId = UUID.randomUUID().toString();
        }

        return roomId;
    }

    private String createNextRoomName()
    {
        PaintChatRoom latestRoom = paintChatRoomRepository.findTopByOrderByRoomNameDesc();

        if (latestRoom == null || latestRoom.getRoomName() == null || latestRoom.getRoomName().trim().isEmpty())
        {
            return "0001";
        }

        try
        {
            int currentNumber = Integer.parseInt(latestRoom.getRoomName());
            return String.format("%04d", currentNumber + 1);
        }
        catch (NumberFormatException exception)
        {
            return "0001";
        }
    }
}
