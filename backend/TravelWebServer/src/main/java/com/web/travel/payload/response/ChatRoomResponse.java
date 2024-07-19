package com.web.travel.payload.response;

import com.web.travel.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomResponse {
    private Long roomId;
    private String avatar;
    private String name;
    private Message newestMessage;
}
