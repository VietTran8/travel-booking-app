package com.web.travel.mapper.request;

import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.dto.request.common.RateUpdateReqDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;
import com.web.travel.service.BlogService;
import com.web.travel.service.TourService;
import com.web.travel.service.UserService;
import com.web.travel.utils.DateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class RateReqMapper implements Mapper {
    @Autowired
    private UserService userService;
    @Autowired
    private TourService tourService;
    @Autowired
    private BlogService blogService;

    @Override
    public Object mapToDTO(Object obj) {
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        Rate rate = new Rate();
        RateReqDTO rateDTO = (RateReqDTO) obj;

        rate.setPoint(rateDTO.getStar());
        rate.setComment(rateDTO.getComment());
        rate.setDateRated(DateHandler.getCurrentDateTime());
        rate.setTour(tourService.findTourById(rateDTO.getTourId()));
        rate.setBlog(blogService.findBlogById(rateDTO.getBlogId()));

        return rate;
    }
}
