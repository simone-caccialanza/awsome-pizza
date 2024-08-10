package com.simocaccia.awsomepizza.repository;

import com.simocaccia.awsomepizza.entity.PizzaOrder;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaOrderRepository extends JpaRepository<PizzaOrder, Long> {
    @Query("SELECT po FROM PizzaOrder po " +
            "WHERE po.orderId = (SELECT MIN(po2.orderId) FROM PizzaOrder po2 WHERE po2.status = :status)")
    Optional<PizzaOrder> findByMinIdAndStatus(@Param("status") PizzaOrderStatus status);

    Optional<PizzaOrder> findByStatus(@Param("status") PizzaOrderStatus status);
}
