package com.web.travel.service.interfaces;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.common.RateReqDTO;
import com.web.travel.dto.request.common.RateUpdateReqDTO;
import com.web.travel.dto.response.ListTourResDTO;
import com.web.travel.dto.response.RateResDTO;
import com.web.travel.model.Tour;
import com.web.travel.payload.response.RateStatistic;

import java.security.Principal;
import java.util.Map;

public interface RateService {
    Map<String, Object> getRates(Principal principal, long id, int page, int limit, boolean isByTour);
    ResDTO addRate(Principal principal, RateReqDTO ratingDTO);
    ResDTO updateRate(Principal principal, RateUpdateReqDTO ratingDTO);
    ResDTO deleteRate(Principal principal, long rateId);
    RateStatistic getStarStatisticByTour(Tour tour);
}
