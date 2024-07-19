package com.web.travel.mapper.response;

import com.web.travel.utils.DateHandler;
import com.web.travel.utils.RateCalculator;
import com.web.travel.dto.response.*;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.*;
import com.web.travel.model.enums.ETourDateType;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TourDetailResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        if(obj instanceof Map<?,?>){
            Tour tour = (Tour) ((Map<?, ?>) obj).get("tour");
            TourBlog tourBlog = (TourBlog) ((Map<?, ?>) obj).get("tourBlog");
            Blog blog = (Blog) ((Map<?, ?>) obj).get("blog");
            List<TourGeneralResDTO> relevantTours = (List<TourGeneralResDTO>) ((Map<?, ?>) obj).get("relevantTours");
            List<Paragraph> paragraphs = (List<Paragraph>) ((Map<?, ?>) obj).get("paragraphs");
            List<Schedule> schedules = (List<Schedule>) ((Map<?, ?>) obj).get("schedules");
            double priceStartFrom = (double) ((Map<?, ?>) obj).get("price");

            TourDetailResDTO tourDto = new TourDetailResDTO();
            tourDto.setId(tour.getId());
            tourDto.setName(tour.getName());
            tourDto.setType(tour.getTourType());
            tourDto.setVehicle(tour.getVehicle());
            tourDto.setDepart(tour.getDepart());
            tourDto.setLocation(tour.getDestination());
            tourDto.setRelevantTours(relevantTours);
            tourDto.setStartFrom(priceStartFrom);

            List<TourDateResDTO> tourDateResDTOs = new ArrayList<>();
            List<TourDate> tourDates = tour.getTourDate().stream().toList();
            DateHandler dateHandler = new DateHandler();
            tourDates.forEach(date -> {
                if(!date.isFull() && date.getDepartDate().getTime() > DateHandler.getCurrentDateTime().getTime()){
                    TourDateResDTO tourDateResDTO = new TourDateResDTO();
                    Date departDate = date.getDepartDate(),
                            endDate = date.getEndDate();
                    tourDateResDTO.setId(date.getId());
                    tourDateResDTO.setDepartDate(departDate);
                    tourDateResDTO.setEndDate(endDate);
                    tourDateResDTO.setAdultPrice(date.getAdultPrice());
                    tourDateResDTO.setChildPrice(date.getChildPrice());
                    tourDateResDTO.setMaxPeople(date.getMaxPeople());
                    tourDateResDTO.setCurrentPeople(date.getCurrentPeople());

                    String type = date.getDateType().toString();
                    if(type.equals(ETourDateType.TYPE_PLUS.toString()))
                        type = "Plus";
                    else
                        type = "Essential";
                    tourDateResDTO.setType(type);
                    int duration = new DateHandler().getDiffDay(endDate, departDate);
                    tourDateResDTO.setDuration(duration);

                    tourDateResDTOs.add(tourDateResDTO);
                }
            });
            tourDto.setTourDate(tourDateResDTOs);
            tourDto.setMaxPeople(tourDates.get(0).getMaxPeople());
            tourDto.setCurrentPeople(tourDates.get(0).getCurrentPeople());
            tourDto.setImg(tour.getImg());

            AtomicInteger index = new AtomicInteger();
            List<ParagraphResDTO> paragraphDTO = new ArrayList<ParagraphResDTO>();
            paragraphs.forEach(paragraph -> {
                ParagraphResDTO paragraphResDTO = new ParagraphResDTO();
                paragraphResDTO.setId(paragraph.getId());
                paragraphResDTO.setContent(paragraph.getContent());
                ParagraphImageResDTO paragraphImageResDTO = new ParagraphImageResDTO();
                if(paragraph.getImgSrc() != null){
                    paragraphImageResDTO.setName(paragraph.getImgName());
                    paragraphImageResDTO.setSrc(paragraph.getImgSrc());
                    paragraphResDTO.setId((long) index.getAndIncrement());
                    paragraphResDTO.setImage(paragraphImageResDTO);
                }else{
                    paragraphResDTO.setImage(null);
                }
                paragraphDTO.add(paragraphResDTO);
            });

            TourBlogResDTO tourBlogResDTO = new TourBlogResDTO();
            tourBlogResDTO.setId(tourBlog.getId());
            tourBlogResDTO.setParagraphs(paragraphDTO);
            if(blog != null)
                tourBlogResDTO.setBackgroundImage(blog.getBackgroundImg());

            tourDto.setOverview(tourBlogResDTO);

            List<HotelResDTO> hotels = new ArrayList<>();
            tour.getHotels().forEach(hotel -> {
                if(hotel != null){
                    HotelResDTO hotelResDTO = new HotelResDTO();
                    hotelResDTO.setId(hotel.getId());
                    hotelResDTO.setName(hotel.getName());
                    hotelResDTO.setIllustration(hotel.getIllustration());
                    hotelResDTO.setAddress(hotel.getAddress());
                    List<RoomResDTO> roomResDTOS = new ArrayList<>();
                    hotel.getRooms().forEach(
                            room -> {
                                RoomResDTO roomResDTO = new RoomResDTO();
                                roomResDTO.setId(room.getId());
                                switch (room.getType()){
                                    case TYPE_MEDIUM -> roomResDTO.setType("Trung bình");
                                    case TYPE_NORMAL -> roomResDTO.setType("Bình thường");
                                    case TYPE_VIP -> roomResDTO.setType("Vip");
                                }
                                roomResDTO.setPrice(room.getPrice());
                                roomResDTOS.add(roomResDTO);
                            }
                    );
                    hotelResDTO.setRooms(roomResDTOS);

                    hotels.add(hotelResDTO);
                }
            });

            tourDto.setHotels(hotels);

            List<ScheduleResDTO> scheduleResDTOS = new ArrayList<>();
            schedules.forEach(schedule -> {
                scheduleResDTOS.add(new ScheduleResDTO(schedule.getId(), schedule.getTime(), schedule.getContent()));
            });

            tourDto.setSchedules(scheduleResDTOS);

            List<Rate> rateObjects = tour.getRates().stream().toList();
            tourDto.setTotalRates(rateObjects.size());
            tourDto.setRatedStar(RateCalculator.getAverageRates(tour.getRates()));
            return tourDto;
        }
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
