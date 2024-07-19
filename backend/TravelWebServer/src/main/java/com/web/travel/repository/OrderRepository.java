package com.web.travel.repository;

import com.web.travel.model.Order;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import com.web.travel.model.enums.EOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByUserAndTour(User user, Tour tour);
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month AND DAY(o.orderDate) = :day AND o.status = :status")
    Double findSumProfitByOrderDate(@Param("year") String year, @Param("month") String month, @Param("day") String day, @Param("status") EOrderStatus orderStatus);
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE MONTH(o.orderDate) = :month AND o.status = :status")
    Double findSumProfitByMonth(@Param("month") String month, @Param("status") EOrderStatus status);
    @Query("SELECT AVG(o.totalPrice) FROM Order o WHERE MONTH(o.orderDate) = :month AND o.status = :status")
    Double findAvgProfitByMonth(@Param("month") String month, @Param("status") EOrderStatus status);
    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.orderDate) = :month")
    Integer findOrderQuantityByMonth(@Param("month") String month);
    @Query("SELECT COUNT(DISTINCT o.user) FROM Order o WHERE MONTH(o.orderDate) = :month")
    Integer findCustomerQuantityByMonth(@Param("month") String month);
}
