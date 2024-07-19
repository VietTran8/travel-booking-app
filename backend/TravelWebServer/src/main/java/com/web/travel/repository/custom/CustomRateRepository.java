package com.web.travel.repository.custom;

import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.payload.response.RateStatistic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class CustomRateRepository {
    @PersistenceContext
    EntityManager entityManager;
    public RateStatistic findRateStatisticByTour(Tour tour){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<Rate> root = criteriaQuery.from(Rate.class);

        criteriaQuery.multiselect(root.get("point"), builder.count(root))
                .where(builder.equal(root.get("tour"), tour))
                .groupBy(root.get("point"))
                .orderBy(builder.desc(root.get("point")));

        List<Object[]> results = entityManager.createQuery(criteriaQuery)
                .getResultList();

        RateStatistic rateStatistic = new RateStatistic();
        results.forEach(item -> {
            switch ((Integer) item[0]){
                case 1 -> {
                    rateStatistic.setOneStar((Long) item[1]);
                }
                case 2 -> {
                    rateStatistic.setTwoStar((Long) item[1]);
                }
                case 3 -> {
                    rateStatistic.setThreeStar((Long) item[1]);
                }
                case 4 -> {
                    rateStatistic.setFourStar((Long) item[1]);
                }
                case 5 -> {
                    rateStatistic.setFiveStar((Long) item[1]);
                }
            }
        });

        return rateStatistic;
    }
}
