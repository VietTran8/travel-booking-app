package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.HotelAddingReqMapper;
import com.web.travel.model.Hotel;
import com.web.travel.model.Room;
import com.web.travel.repository.HotelRepository;
import com.web.travel.repository.RoomRepository;
import com.web.travel.service.interfaces.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class HotelService {
    @Autowired
    private HotelRepository repository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private RoomRepository roomRepository;
    public Hotel getHotelById(long id){
        return repository.findById(id).orElse(null);
    }
    public Page<Hotel> getAllHotel(int page, int limit){
        return repository.findAll(PageRequest.of(page, limit));
    }

    public ResDTO getAllHotelRes(int page, int limit){
        Map<String, Object> response = new HashMap<>();
        Page<Hotel> result = getAllHotel(page, limit);
        response.put("pages", result.getTotalPages());
        response.put("hotels", result.getContent());

        return new ResDTO(
                200,
                true,
                "Lấy khách sạn thành công",
                response
        );
    }

    public ResDTO addHotel(
            MultipartFile image,
            HotelAddingDTO hotelAddingDTO
    ){
        String fileName = null;
        if(image != null){
            try {
                fileName = fileUploadService.uploadFile(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Mapper hotelMapper = new HotelAddingReqMapper();
        Hotel hotel = (Hotel) hotelMapper.mapToObject(hotelAddingDTO);

        hotel.setIllustration(fileName);

        return new ResDTO(
                200,
                true,
                "Thêm mới khách sạn thành công!",
                repository.save(hotel)
        );
    }

    public ResDTO updateHotel(Long id, MultipartFile image,
                       HotelAddingDTO hotelAddingDTO
    ){
        String fileName = null;
        if(image != null){
            try {
                if(!Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
                    fileName = fileUploadService.uploadFile(image);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Mapper hotelMapper = new HotelAddingReqMapper();
        Hotel hotel = (Hotel) hotelMapper.mapToObject(hotelAddingDTO);

        Optional<Hotel> optionalHotel = repository.findById(id);
        if(optionalHotel.isPresent()){
            Hotel hotel1 = optionalHotel.get();
            Collection<Room> oldRooms = hotel1.getRooms();
            oldRooms.clear();
            hotel1.setName(hotel.getName());
            hotel1.setAddress(hotel.getAddress());
            if(fileName != null)
                hotel1.setIllustration(fileName);

            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel1);
                oldRooms.add(room);
            }

            repository.save(hotel1);
            return new ResDTO(
                    200,
                    true,
                    "Cập nhật khách sạn thành công!",
                    hotel1
            );
        }
        return new ResDTO(
                400,
                false,
                "Không tìm thấy khách sạn có id: " + id,
                null);
    }

    public ResDTO deleteHotel(Long id){
        Optional<Hotel> optionalHotel = repository.findById(id);
        if(optionalHotel.isPresent()){
            repository.delete(optionalHotel.get());
            return new ResDTO(
                    200,
                    true,
                    "Xóa khách sạn thành công!",
                    null
            );
        }
        return new ResDTO(
                400,
                false,
                "Hotel not found with id: " + id,
                null
        );
    }

    public ResDTO getByAddress(String address){
        List<Hotel> hotels = repository.findByAddressContaining(address);
        return new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Hotels fetched successfully",
                hotels
        );
    }
}
