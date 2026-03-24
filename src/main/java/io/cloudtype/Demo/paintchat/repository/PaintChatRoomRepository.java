package io.cloudtype.Demo.paintchat.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.cloudtype.Demo.paintchat.model.PaintChatRoom;

public interface PaintChatRoomRepository extends JpaRepository<PaintChatRoom, Long>
{
    List<PaintChatRoom> findAllByOrderByIdDesc();

    PaintChatRoom findByRoomId(String roomId);

    boolean existsByRoomId(String roomId);

    PaintChatRoom findTopByOrderByRoomNameDesc();

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
