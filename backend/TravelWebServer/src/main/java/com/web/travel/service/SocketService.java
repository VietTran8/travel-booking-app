package com.web.travel.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.web.travel.model.Message;
import com.web.travel.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    public final MessageService messageService;
    public final UserService userService;

    public void sendSocketMessage(SocketIOClient senderClient, Object message, Long room, boolean isOnConnected){
        senderClient
                .getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(client -> {
                    boolean isAdmin = client.get("isAdmin") != null ? client.get("isAdmin") : false;
                    if((isOnConnected && !isAdmin) || !client.getSessionId().equals(senderClient.getSessionId())){
                        client.sendEvent("receive", message);

                    }
                });


        JSONObject content = new JSONObject();
        content.put("app_id", "a0f4fabe-9b2a-432e-8233-c944f2edcfcb");
        content.put("included_segments", "Total Subscriptions");
        JSONObject contents = new JSONObject();
        contents.put("en", ((Message) message).getMessage());
        content.put("small_icon", "https://lh3.google.com/u/0/d/1TreLyjVQA8XPV85rkydhMRdmiCQRgGxY=w1920-h959-iv2");
        content.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic NDc5YTVhNTktYmE2Yy00NmFhLWE2ZGQtZDgwY2IwZjE0ODQ1");
        headers.set("Content-Type", "application/json");

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(content, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://onesignal.com/api/v1/notifications", requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("Response body: " + responseBody);



        sendNotification(senderClient, message);
    }

    public void sendNotification(SocketIOClient senderClient, Object message){
        senderClient.getNamespace()
                .getAllClients()
                .forEach(client -> {
                    boolean isAdmin = client.get("isAdmin") != null ? client.get("isAdmin") : false;
                    if(isAdmin)
                        client.sendEvent("new-message", message);
                });
    }

    public void sendGetAllMessages(SocketIOClient senderClient, Object message, Long room){
        senderClient
                .getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(client -> {
                    if(client.getSessionId().equals(senderClient.getSessionId())){
                        client.sendEvent("connected", message);
                    }
                });
    }

    public void sendChangeEvent(SocketIOClient senderClient, Object message, Long room){
        senderClient.getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(socketIOClient -> {
                    if(!socketIOClient.getSessionId().equals(senderClient.getSessionId()))
                        socketIOClient.sendEvent("change", message);
                });
    }

    public void sendStopChangeEvent(SocketIOClient senderClient, Object message, Long room){
        senderClient.getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(socketIOClient -> {
                    if(!socketIOClient.getSessionId().equals(senderClient.getSessionId()))
                        socketIOClient.sendEvent("stop-change", message);
                });
    }

    public void saveMessage(SocketIOClient senderClient, Message message){
        User user = userService.getUserObjectById(message.getUid());
        String avatar = "";
        if(user != null)
            avatar = user.getAvatar() != null ? user.getAvatar() : "";
        Message savedMessage = messageService.saveMessage(
                Message.builder()
                        .message(message.getMessage())
                        .room(message.getRoom())
                        .time(message.getTime())
                        .uid(message.getUid())
                        .role(message.getRole().toUpperCase())
                        .avatar(avatar)
                        .name(
                                (user != null && user.getFullName() != null)
                                        ? user.getFullName()
                                        : "Unknown"
                        )
                        .build()
        );

        sendSocketMessage(senderClient, savedMessage, message.getRoom(), false);
    }

    public void sendConnectedMessage(SocketIOClient senderClient, Long room) {
        List<Message> messages = messageService.getMessages(room);
        sendGetAllMessages(senderClient, messages, room);
    }
}
