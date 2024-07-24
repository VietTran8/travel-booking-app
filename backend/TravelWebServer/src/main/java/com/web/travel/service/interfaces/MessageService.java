package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessages(Long room);

    Message saveMessage(Message message);

    ResDTO getRooms();
}
