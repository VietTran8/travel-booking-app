package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Message;
import com.web.travel.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/message")
@RequiredArgsConstructor
public class ChatRoomController {
    private final MessageService messageService;

    @CrossOrigin(origins = "*")
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/rooms")
    public ResponseEntity<?> getRooms(){
        return ResponseEntity.ok(
                messageService.getRooms()
        );
    }
}
