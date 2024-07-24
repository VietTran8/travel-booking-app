package com.web.travel.service.interfaces;

import com.corundumstudio.socketio.SocketIOClient;
import com.web.travel.model.Message;

public interface SocketService {
    void sendSocketMessage(SocketIOClient senderClient, Object message, Long room, boolean isOnConnected);
    void sendNotification(SocketIOClient senderClient, Object message);
    void sendGetAllMessages(SocketIOClient senderClient, Object message, Long room);
    void sendChangeEvent(SocketIOClient senderClient, Object message, Long room);
    void sendStopChangeEvent(SocketIOClient senderClient, Object message, Long room);
    void saveMessage(SocketIOClient senderClient, Message message);
    void sendConnectedMessage(SocketIOClient senderClient, Long room);
}
