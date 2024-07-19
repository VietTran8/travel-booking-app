package com.web.travel.repository.custom;

import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.payload.response.TopDestinationResponse;
import com.web.travel.utils.RateCalculator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomTourRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Tour> getRelevantTourByDestination(String depart, String destination, int numberOfTour){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = builder.createQuery(Tour.class);
        Root<Tour> root = criteriaQuery.from(Tour.class);

        String normalDestinationStr = StringUtils.stripAccents(destination),
            normalDepartStr = StringUtils.stripAccents(depart);
        normalDestinationStr = normalDestinationStr.toLowerCase();
        normalDepartStr = normalDepartStr.toLowerCase();
        Predicate destinationLikePredicate = builder.like(root.get("destination"), '%' + normalDestinationStr + '%');
        Predicate departLikePredicate = builder.like(root.get("depart"), '%' + normalDepartStr + '%');
        Predicate desLikeOrDepartLike = builder.or(destinationLikePredicate, departLikePredicate);

        criteriaQuery.select(root).where(desLikeOrDepartLike);

        return entityManager.createQuery(criteriaQuery)
                .setMaxResults(numberOfTour)
                .getResultList();
    }

    public List<TopDestinationResponse> findTopDestinations(int top) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Tour> tourRoot = criteriaQuery.from(Tour.class);
        Join<Tour, Order> orderJoin = tourRoot.join("orders", JoinType.INNER);

        criteriaQuery.multiselect(tourRoot.get("destination"), criteriaBuilder.count(orderJoin), tourRoot.get("img"));
        criteriaQuery.groupBy(tourRoot.get("destination"));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(orderJoin)));

        List<Object[]> results = entityManager.createQuery(criteriaQuery)
                .setMaxResults(top)
                .getResultList();

        List<TopDestinationResponse> topDestinations = new ArrayList<>();
        for (Object[] result : results) {
            TopDestinationResponse response = new TopDestinationResponse();
            response.setId(UUID.randomUUID().toString());
            response.setName((String) result[0]);
            response.setOrderQuantity((Long) result[1]);
            response.setImg((String) result[2]);
            topDestinations.add(response);
        }

        return topDestinations;
    }

    public double findTopRatedByDestination(String destination){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = builder.createQuery(Tour.class);
        Root<Tour> root = criteriaQuery.from(Tour.class);
        Join<Tour, Rate> rateJoin = root.join("rates", JoinType.INNER);

        criteriaQuery
                .select(root)
                .where(builder.equal(root.get("destination"), destination))
                .groupBy(root.get("id"))
                .orderBy(builder.desc(builder.avg(rateJoin.get("point"))));

        List<Tour> tours = entityManager.createQuery(criteriaQuery).getResultList();
        if (!tours.isEmpty()) {
            Tour tour = tours.get(0);
            List<Rate> rateObjects = tour.getRates().stream().toList();

            return RateCalculator.getAverageRates(rateObjects);
        }
        return 0;
    }

}
