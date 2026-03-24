package io.cloudtype.Demo.paintchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.cloudtype.Demo.paintchat.domain.PaintChatStroke;

public interface PaintChatStrokeRepository extends JpaRepository<PaintChatStroke, Long>
{
    List<PaintChatStroke> findByRoomIdOrderByIdAsc(String roomId);

    long countByRoomId(String roomId);
}
