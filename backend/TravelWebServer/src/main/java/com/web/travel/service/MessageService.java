package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Message;
import com.web.travel.model.User;
import com.web.travel.payload.response.ChatRoomResponse;
import com.web.travel.repository.MessageRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    public List<Message> getMessages(Long room){
        return messageRepository.findAllByRoom(room);
    }
    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public ResDTO getRooms(){
        List<Long> rooms = messageRepository.findRooms();
        List<ChatRoomResponse> response = new ArrayList<>();
        rooms.forEach(room -> {
            ChatRoomResponse each  = new ChatRoomResponse();
            each.setRoomId(room);
            Message newestMessage = messageRepository.findLastMessageByRoom(room);
            each.setNewestMessage(newestMessage);
            User user = userService.getUserObjectById(room);
            if(user != null){
                each.setAvatar(user.getAvatar() != null ? user.getAvatar() : "");
                each.setName(user.getFullName());
            }else{
                each.setName("");
                each.setAvatar("");
            }
            response.add(each);
        });

        return new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Rooms fetched successfully",
                response
        );
    }
}
