package com.web.travel.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JoinRoomRequest {
    private Long room;
    private Boolean admin;
}
