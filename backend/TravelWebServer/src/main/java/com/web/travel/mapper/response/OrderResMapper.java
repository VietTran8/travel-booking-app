package com.web.travel.mapper.response;

import com.web.travel.dto.response.HotelResDTO;
import com.web.travel.dto.response.OrderDetailResDTO;
import com.web.travel.dto.response.RoomResDTO;
import com.web.travel.dto.response.TourGeneralResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Hotel;
import com.web.travel.model.Order;
import com.web.travel.model.enums.EOrderStatus;
import com.web.travel.model.enums.EPaymentMethod;
import com.web.travel.utils.DateHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        Order order = (Order) obj;
        OrderDetailResDTO dto = new OrderDetailResDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAdults(order.getAdults());
        dto.setSpecialRequest(order.getSpecialRequest());
        dto.setChildren(order.getChildren());

        HotelResDTO hotelDto = new HotelResDTO();
        Hotel foundHotel = order.getHotel();
        dto.setHotel(null);
        if(foundHotel != null){
            hotelDto.setName(foundHotel.getName());
            hotelDto.setAddress(foundHotel.getAddress());
            hotelDto.setIllustration(foundHotel.getIllustration());
            hotelDto.setId(foundHotel.getId());
            List<RoomResDTO> roomResDTOS = new ArrayList<>();

            foundHotel.getRooms().forEach(room -> {
                RoomResDTO roomResDTO = new RoomResDTO();
                roomResDTO.setId(room.getId());
                switch (room.getType()){
                    case TYPE_MEDIUM -> roomResDTO.setType("Trung bình");
                    case TYPE_NORMAL -> roomResDTO.setType("Bình thường");
                    case TYPE_VIP -> roomResDTO.setType("Vip");
                }
                roomResDTO.setPrice(room.getPrice());
                roomResDTOS.add(roomResDTO);
            });

            hotelDto.setRooms(roomResDTOS);
            dto.setHotel(hotelDto);
        }

        TourGeneralResMapper tourMapper = new TourGeneralResMapper();
        TourGeneralResDTO tourDto = (TourGeneralResDTO) tourMapper.mapToDTO(order.getTour());
        DateHandler dateHandler = new DateHandler();
        tourDto.setDuration(dateHandler.getDiffDay(
                order.getTourDate().getEndDate(), order.getTourDate().getDepartDate()));
        dto.setTour(tourDto);

        if(order.getStatus() == EOrderStatus.STATUS_CANCELED)
            dto.setStatus("Đã hủy");
        else if(order.getStatus() == EOrderStatus.STATUS_ORDERED)
            dto.setStatus("Đã thanh toán");
        else
            dto.setStatus("Đang chờ");

        dto.setContactInfo(order.getContactInfo());
        dto.setDepartDate(order.getTourDate().getDepartDate());
        dto.setEndDate(order.getTourDate().getEndDate());

        if(order.getPaymentMethod() == EPaymentMethod.METHOD_VNPAY)
            dto.setPaymentMethod("Thanh toán bằng VNPay");
        else if(order.getPaymentMethod() == EPaymentMethod.METHOD_PAYPAL)
            dto.setPaymentMethod("Thanh toán bằng Paypal");
        else
            dto.setPaymentMethod("Thanh toán bằng tiền mặt");

        dto.setTotalPrice(order.getTotalPrice());

        return dto;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
