package com.web.travel.mapper.request;

import com.web.travel.dto.request.admin.tour.ScheduleAddingDTO;
import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Hotel;
import com.web.travel.model.Schedule;
import com.web.travel.model.Tour;
import com.web.travel.model.TourDate;
import com.web.travel.model.enums.ETourDateType;
import com.web.travel.model.enums.ETourType;
import com.web.travel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TourAddingReqMapper implements Mapper {
    @Autowired
    private HotelService hotelService;
    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        TourAddingDTO tourAddingDTO = (TourAddingDTO) obj;
        Tour tour = new Tour();
        tour.setName(tourAddingDTO.getName());
        tour.setVehicle(tourAddingDTO.getVehicle());
        tour.setTourType(ETourType.TYPE_NORMAL);
        tour.setDepart(tourAddingDTO.getDepart());
        tour.setDestination(tourAddingDTO.getDestination());

        List<TourDate> tourDates = new ArrayList<>();
        if(tourAddingDTO.getTourDate() != null)
            tourAddingDTO.getTourDate().forEach(date -> {
                TourDate tourDate = new TourDate();
                if(date.getDateType() != null)
                    switch (date.getDateType()){
                        case "essential" -> {
                            tourDate.setDateType(ETourDateType.TYPE_ESSENTIAL);
                        }case "plus" -> {
                            tourDate.setDateType(ETourDateType.TYPE_PLUS);
                        }
                    }

                tourDate.setDepartDate(date.getDepartDate());
                tourDate.setEndDate(date.getEndDate());
                tourDate.setAdultPrice(date.getAdultPrice());
                tourDate.setChildPrice(date.getChildPrice());
                tourDate.setTour(tour);
                tourDate.setCurrentPeople(date.getCurrentPeople());
                tourDate.setMaxPeople(date.getMaxPeople());
                tourDates.add(tourDate);
            });

        tour.setTourDate(tourDates);

        List<Schedule> schedules = new ArrayList<>();

        List<ScheduleAddingDTO> scheduleAddingDTOS = tourAddingDTO.getSchedules();
        if(scheduleAddingDTOS != null){
            TourAddingReqMapper.sortScheduleByOrderAsc(scheduleAddingDTOS);

            scheduleAddingDTOS.forEach(scheduleDTO -> {
                Schedule schedule = new Schedule();
                schedule.setTime(scheduleDTO.getTime());
                schedule.setContent(scheduleDTO.getContent());
                schedule.setTour(tour);
                schedules.add(schedule);
            });

            tour.setSchedules(schedules);
        }

        List<Hotel> hotels = new ArrayList<>();
        tourAddingDTO.getHotelIds().forEach(id -> {
            Hotel hotel = hotelService.getHotelById(id);
            hotels.add(hotel);
        });
        tour.setHotels(hotels);
        return tour;
    }

    private static void sortScheduleByOrderAsc(List<ScheduleAddingDTO> scheduleAddingDTOS) {
        scheduleAddingDTOS.sort(new Comparator<ScheduleAddingDTO>() {
            @Override
            public int compare(ScheduleAddingDTO o1, ScheduleAddingDTO o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }
}

