package com.web.travel.mapper.request;

import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;
import com.web.travel.service.impl.TourServiceImpl;
import com.web.travel.service.impl.UserServiceImpl;
import com.web.travel.service.interfaces.BlogService;
import com.web.travel.service.interfaces.TourService;
import com.web.travel.service.interfaces.UserService;
import com.web.travel.utils.DateHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RateReqMapper implements Mapper {
    private UserService userService;
    private TourService tourService;
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
