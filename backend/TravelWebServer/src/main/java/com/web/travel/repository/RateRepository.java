package com.web.travel.repository;

import com.web.travel.model.DestinationBlog;
import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    public Page<Rate> findByTourOrderByDateRatedDesc(Tour tour, Pageable pageable);
    public Page<Rate> findByBlogOrderByDateRatedDesc(DestinationBlog blog, Pageable pageable);

}
