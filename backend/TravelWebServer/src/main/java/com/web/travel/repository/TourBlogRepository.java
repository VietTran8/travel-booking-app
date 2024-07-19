package com.web.travel.repository;

import com.web.travel.model.Blog;
import com.web.travel.model.Tour;
import com.web.travel.model.TourBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourBlogRepository extends JpaRepository<TourBlog, Long> {
    public Optional<TourBlog> findByTour(Tour tour);
}
