package com.simocaccia.awsomepizza.service;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrder;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;

import java.util.Collection;
import java.util.List;


public interface PizzaService {
    Long takeOrder(Collection<PizzaName> items);

    PizzaOrderStatus checkOrderStatus(Long id);

    Long completeStartedOrder();

    Long startAcceptedOrder();

    List<PizzaOrder> getAllOrders();
}
