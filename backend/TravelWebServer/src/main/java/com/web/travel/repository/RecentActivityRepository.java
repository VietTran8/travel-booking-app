package com.web.travel.repository;

import com.web.travel.model.DestinationBlog;
import com.web.travel.model.RecentActivity;
import com.web.travel.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RecentActivityRepository extends JpaRepository<RecentActivity, Long> {
    boolean existsByBlog(DestinationBlog blog);
    boolean existsByTour(Tour tour);
    Optional<RecentActivity> findByBlog(DestinationBlog blog);
    Optional<RecentActivity> findByTour(Tour tour);
    @Modifying
    @Transactional
    @Query("DELETE FROM RecentActivity r WHERE r.id = :id")
    void deleteRecentById(@Param("id") Long id);
}
