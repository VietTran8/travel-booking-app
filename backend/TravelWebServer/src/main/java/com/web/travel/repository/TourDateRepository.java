package com.web.travel.repository;

import com.web.travel.model.Tour;
import com.web.travel.model.TourDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDateRepository extends JpaRepository<TourDate, Long> {
    void deleteByTour(Tour tour);
}
