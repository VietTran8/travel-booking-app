package com.web.travel.controller.admin;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.hotel.HotelAddingDTO;
import com.web.travel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/hotel")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HotelAdminController {
    private final HotelService hotelService;

    @GetMapping("")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getAllHotel(
            @RequestParam(defaultValue = "", required = false) String address,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "1000") int limit
    ){
       return (address != null && !address.isEmpty()) ?
               ResponseEntity.ok(
                       hotelService.getByAddress(address)
               ) :
               ResponseEntity.ok(
                       hotelService.getAllHotelRes(page - 1, limit)
               );
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addHotel(
            @RequestPart("image") MultipartFile image,
            @RequestPart("hotel") HotelAddingDTO hotelAddingDTO
    ){
        return ResponseEntity.ok(
                hotelService.addHotel(image, hotelAddingDTO)
        );
    }

    @PostMapping("update/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateHotel(
            @PathVariable("id") long id,
            @RequestPart("image") MultipartFile image,
            @RequestPart("hotel") HotelAddingDTO hotelAddingDTO
    ){
        ResDTO response = hotelService.updateHotel(id, image, hotelAddingDTO);

        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("delete/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteHotel(@PathVariable long id){
        ResDTO response = hotelService.deleteHotel(id);

        if(response.isStatus())
            return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
