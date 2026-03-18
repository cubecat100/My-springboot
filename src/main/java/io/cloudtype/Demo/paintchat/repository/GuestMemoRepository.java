package io.cloudtype.Demo.paintchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.cloudtype.Demo.paintchat.domain.GuestMemo;

public interface GuestMemoRepository extends JpaRepository<GuestMemo, Long>
{
    List<GuestMemo> findAllByOrderByIdDesc();
}
