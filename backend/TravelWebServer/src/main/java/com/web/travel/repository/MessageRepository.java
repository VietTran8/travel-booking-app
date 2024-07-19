package com.web.travel.repository;

import com.web.travel.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAllByRoom(Long room);
    @Query("SELECT m FROM Message m WHERE m.room = :room ORDER BY m.time DESC LIMIT 1")
    public Message findLastMessageByRoom(@Param("room") Long room);
    @Query("SELECT m.room FROM Message m GROUP BY m.room")
    public List<Long> findRooms();
}
