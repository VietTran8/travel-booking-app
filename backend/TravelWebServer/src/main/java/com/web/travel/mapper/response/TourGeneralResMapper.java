package com.web.travel.mapper.response;

import com.web.travel.model.enums.ETourType;
import com.web.travel.utils.DateHandler;
import com.web.travel.utils.RateCalculator;
import com.web.travel.dto.response.TourGeneralResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.model.TourDate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class TourGeneralResMapper implements Mapper {
    TourGeneralResDTO tourGeneralResDTO = new TourGeneralResDTO();
    DateHandler dateHandler = new DateHandler();
    @Override
    public Object mapToDTO(Object obj) {
        if(obj != null){
            tourGeneralResDTO.setId(((Tour) obj).getId());
            tourGeneralResDTO.setImg(((Tour) obj).getImg());
            tourGeneralResDTO.setName(((Tour) obj).getName());
            tourGeneralResDTO.setLocation(((Tour) obj).getDestination());
            tourGeneralResDTO.setDepart(((Tour) obj).getDepart());
            TourDate tourDate = ((Tour) obj).getTourDate().stream().findFirst().orElse(null);
            tourGeneralResDTO.setMaxPeople(0);
            double price = 0;
            int duration = 0;
            if(tourDate != null){
                Date depart = tourDate.getDepartDate(),
                        end = tourDate.getEndDate();
                duration = dateHandler.getDiffDay(end, depart);

                price = tourDate.getAdultPrice();
                tourGeneralResDTO.setMaxPeople(tourDate.getMaxPeople());
            }
            tourGeneralResDTO.setPrice(price);
            tourGeneralResDTO.setDuration(duration);
            String typeDto = "saving";
            String typeTitle = "Tour tiết kiệm";
            ETourType tourType = ((Tour) obj).getTourType();
            if(tourType != null)
                switch (((Tour) obj).getTourType().toString()) {
                    case "TYPE_SAVING" -> {
                        typeDto = "saving";
                        typeTitle = "Tour tiết kiệm";
                    }
                    case "TYPE_SPECIAL" -> {
                        typeDto = "special";
                        typeTitle = "Tour đặc biệt";
                    }
                    case "TYPE_NORMAL" -> {
                        typeTitle = "Tour thông thường";
                        typeDto = "normal";
                    }
                    case "TYPE_POPULAR" -> {
                        typeTitle = "Tour phổ biến";
                        typeDto = "popular";
                    }
                    default -> {
                    }
                }
            tourGeneralResDTO.setType(typeDto);
            tourGeneralResDTO.setTypeTitle(typeTitle);

            Collection<Rate> rates = ((Tour) obj).getRates();

            tourGeneralResDTO.setRatedStar(RateCalculator.getAverageRates(rates));
            return tourGeneralResDTO;
        }
        return null;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
