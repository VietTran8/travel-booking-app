package com.web.travel.mapper.request;

import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Hotel;
import com.web.travel.model.Room;
import com.web.travel.model.enums.ERoom;

import java.util.ArrayList;
import java.util.List;

public class HotelAddingReqMapper implements Mapper {

    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        HotelAddingDTO hotelDTO = (HotelAddingDTO) obj;
        Hotel hotel = new Hotel();

        hotel.setAddress(hotelDTO.getAddress());
        hotel.setName(hotelDTO.getName());

        List<Room> rooms = new ArrayList<>();
        hotelDTO.getRooms().forEach(roomDTO -> {
            Room room = new Room();
            switch (roomDTO.getType()){
                case "normal" -> {
                    room.setType(ERoom.TYPE_NORMAL);
                }case "vip" -> {
                    room.setType(ERoom.TYPE_VIP);
                }case "medium" -> {
                    room.setType(ERoom.TYPE_MEDIUM);
                }
            }

            room.setPrice(roomDTO.getPrice());
            room.setHotel(hotel);

            rooms.add(room);
        });

        hotel.setRooms(rooms);

        return hotel;
    }
}
