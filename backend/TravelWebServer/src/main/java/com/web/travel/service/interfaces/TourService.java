package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.tour.TourAddingDTO;
import com.web.travel.dto.response.ListTourResDTO;
import com.web.travel.dto.response.TourDetailResDTO;
import com.web.travel.dto.response.TourGeneralResDTO;
import com.web.travel.model.Tour;
import com.web.travel.payload.request.TourFilter;
import com.web.travel.payload.response.TopDestinationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface TourService {
    List<ListTourResDTO> getTourDTOListGroupByType();

    Map<String, Object> getAllTour(int page, int limit);

    ResDTO getTourResByNameOrDestination(String keyWord, int page, int limit);

    ResDTO getTourByFilter(TourFilter tourFilter);

    Tour findTourById(Long id);

    List<TourGeneralResDTO> findTourByType(String type);

    long getCount();

    Object getResponseTourById(Principal principal, Long id);

    List<TourGeneralResDTO> getRelevantToursByDestination(String depart, String destination, int numberOfTour);

    ResDTO add(TourAddingDTO tour, MultipartFile[] images);

    ResDTO updateTour(long id, TourAddingDTO tour, MultipartFile[] images);

    Map<String, Object> adminGetAllTour(Principal principal, int page, int limit);

    ResDTO deleteTour(long id);

    ResDTO getTopDestinations(int top);

    ResDTO getTourDate(long tourId);

    ResDTO searchContainBlog(String key);
}
