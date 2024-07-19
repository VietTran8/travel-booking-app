package com.web.travel.mapper.response;

import com.web.travel.dto.response.RateResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;

public class RateResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        Rate rate = (Rate) obj;
        RateResDTO rateResDTO = new RateResDTO();
        rateResDTO.setRatedDate(rate.getDateRated());
        rateResDTO.setStar(rate.getPoint());
        rateResDTO.setComment(rate.getComment());
        rateResDTO.setUsername(rate.getUser().getFullName());
        rateResDTO.setEmail(rate.getUser().getEmail());
        rateResDTO.setId(rate.getId());
        rateResDTO.setAvatar(rate.getUser().getAvatar());
        return rateResDTO;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
