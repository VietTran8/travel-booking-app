package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.dto.request.common.RateUpdateReqDTO;
import com.web.travel.dto.response.ListTourResDTO;
import com.web.travel.dto.response.RateResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.mapper.request.RateReqMapper;
import com.web.travel.mapper.response.RateResMapper;
import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.payload.response.RateStatistic;
import com.web.travel.repository.DestinationBlogRepository;
import com.web.travel.repository.RateRepository;
import com.web.travel.repository.TourRepository;
import com.web.travel.repository.UserRepository;
import com.web.travel.repository.custom.CustomRateRepository;
import com.web.travel.utils.DateHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RateService {
    @Autowired
    TourRepository tourRepository;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DestinationBlogRepository desRepository;
    @Autowired
    CustomRateRepository customRateRepository;
    @Autowired
    RateReqMapper reqMapper;
    public Map<String, Object> getRates(Principal principal, long id, int page, int limit, boolean isByTour){
        Tour foundTour = null;
        DestinationBlog foundBlog = null;
        if(isByTour)
            foundTour = tourRepository.findById(id).orElse(null);
        else
            foundBlog = desRepository.findById(id).orElse(null);
        if (foundTour != null || foundBlog != null) {
            Page<Rate> ratesPage;
            if(isByTour)
                ratesPage = rateRepository
                        .findByTourOrderByDateRatedDesc(foundTour, PageRequest.of(page - 1, limit));
            else
                ratesPage = rateRepository
                        .findByBlogOrderByDateRatedDesc(foundBlog, PageRequest.of(page - 1, limit));

            Map<String, Object> response = new HashMap<>();
            response.put("pages", ratesPage.getTotalPages());

            if (foundTour != null){
                RateStatistic rateStatistic = getStarStatisticByTour(foundTour);
                response.put("starDistribution", rateStatistic);
                response.put("totalStar", rateStatistic.getTotalRates());
                response.put("average", rateStatistic.getAverage());
            }

            List<RateResDTO> rates;
            Stream<Rate> rateStream;

            if(principal != null){
                List<Rate> preHandlingRates = new ArrayList<>(ratesPage.get().toList());
                List<Rate> userRates = new ArrayList<>(preHandlingRates.stream().filter(rate ->
                            rate.getUser().getEmail() != null && rate.getUser().getEmail().equals(principal.getName())
                ).toList());
                preHandlingRates.removeAll(userRates);
                userRates.addAll(preHandlingRates);

                rateStream = userRates.stream();
            }else{
                rateStream = ratesPage.get();
            }

            rates = rateStream.map(rate -> {
                RateResMapper mapper = new RateResMapper();
                RateResDTO dto = (RateResDTO) mapper.mapToDTO(rate);
                dto.setActive(
                    (principal != null && principal.getName().equals(dto.getEmail()))
                );
                return dto;
            }).toList();

            response.put("rates", rates);
            return response;
        }
        return null;
    }

    public ResDTO addRate(Principal principal, RateReqDTO ratingDTO){
        Rate rate = (Rate) reqMapper.mapToObject(ratingDTO);
        rate.setUser(userRepository.findByEmail(principal.getName()).orElse(null));
        Map<String, RateResDTO> response = new HashMap<>();
        RateResMapper mapper = new RateResMapper();
        RateResDTO rateResDTO = ((RateResDTO) mapper.mapToDTO(rateRepository.save(rate)));
        rateResDTO.setActive(true);
        response.put(
                "rateAdded",
                rateResDTO
            );

        return new ResDTO(
            200,
            true,
            "Đã thêm bình luận",
            response
        );
    }

    public ResDTO updateRate(Principal principal, RateUpdateReqDTO ratingDTO){
        Rate needUpdateRate = rateRepository.findById(ratingDTO.getId()).orElse(null);
        if (needUpdateRate != null){
            if(principal.getName().equals(needUpdateRate.getUser().getEmail())) {
                needUpdateRate.setComment(ratingDTO.getComment());
                needUpdateRate.setPoint(ratingDTO.getStar());
                needUpdateRate.setDateRated(DateHandler.getCurrentDateTime());

                Rate updatedRate = rateRepository.save(needUpdateRate);
                RateResMapper mapper = new RateResMapper();
                Map<String, Object> response = new HashMap<>();
                RateResDTO dto = (RateResDTO) mapper.mapToDTO(updatedRate);
                dto.setActive(true);
                response.put(
                        "rateUpdated",
                        dto
                );
                return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Cập nhật đánh giá thành công!",
                        response
                );
            }
            return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Bạn không thể cập nhật đánh giá của người khác!",
                null
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy đánh giá!",
            null
        );
    }

    public ResDTO deleteRate(Principal principal, long rateId){
        Rate rate = rateRepository.findById(rateId).orElse(null);
        if(rate != null) {
            if (rate.getUser().getEmail().equals(principal.getName())) {
                rateRepository.deleteById(rateId);
                Map<String, Long> response = new HashMap<>();
                response.put("deletedId", rate.getId());
                return new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Xóa đánh giá thành công!",
                        response
                );
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Bạn không thể xóa bình luận của người khác!",
                    null
            );
        }
        return new ResDTO(
            HttpServletResponse.SC_BAD_REQUEST,
            false,
            "Không tìm thấy bình luận!",
            null
        );
    }

    public RateStatistic getStarStatisticByTour(Tour tour){
        if(tour != null){
            return customRateRepository.findRateStatisticByTour(tour);
        }
        return null;
    }
}
