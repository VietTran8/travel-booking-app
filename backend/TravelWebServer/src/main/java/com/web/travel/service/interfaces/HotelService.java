package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface HotelService {

    Hotel getHotelById(long id);

    Page<Hotel> getAllHotel(int page, int limit);

    ResDTO getAllHotelRes(int page, int limit);

    ResDTO addHotel(MultipartFile image, HotelAddingDTO hotelAddingDTO);

    ResDTO updateHotel(Long id, MultipartFile image, HotelAddingDTO hotelAddingDTO);

    ResDTO deleteHotel(Long id);

    ResDTO getByAddress(String address);
}
