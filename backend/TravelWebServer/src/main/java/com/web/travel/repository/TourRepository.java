package com.web.travel.repository;

import com.web.travel.model.Tour;
import com.web.travel.model.enums.ETourType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByTourType(ETourType tourType);
    Page<Tour> findByNameContainingOrDestinationContaining(String name, String destination, Pageable pageable);
    List<Tour> findByNameContainingOrDestinationContaining(String name, String destination);
    @Query("SELECT DISTINCT t FROM Tour t " +
            "JOIN t.rates r " +
            "JOIN t.tourDate td " +
            "WHERE (:star IS NULL OR t.id IN (" +
            "   SELECT tr.id FROM Tour tr " +
            "   JOIN tr.rates rr " +
            "   GROUP BY tr.id " +
            "   HAVING ROUND(AVG(rr.point), 0) = :star" +
            ")) AND " +
            "(:departDate IS NULL OR :endDate IS NULL OR DATE(td.departDate) >= DATE(:departDate) AND DATE(td.endDate) <= DATE(:endDate)) AND " +
            "(:fromPrice IS NULL OR :toPrice IS NULL OR td.adultPrice >= :fromPrice AND td.adultPrice <= :toPrice) AND " +
            "(t.isRemoved = false OR t.isRemoved IS NULL) AND " +
            "(:type IS NULL OR t.tourType = :type)")
    Page<Tour> findByFilter(
            @Param("star") Integer star,
            @Param("departDate") Date departDate,
            @Param("endDate") Date endDate,
            @Param("fromPrice") Double fromPrice,
            @Param("toPrice") Double toPrice,
            @Param("type") ETourType type,
            Pageable pageable
    );
}
