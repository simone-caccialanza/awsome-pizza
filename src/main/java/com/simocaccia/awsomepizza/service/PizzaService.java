package com.simocaccia.awsomepizza.service;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;

import java.util.Collection;


public interface PizzaService {
    Long takeOrder(Collection<PizzaName> items);

    PizzaOrderStatus checkOrderStatus(Long id);

    Long completeStartedOrder();

    Long startAcceptedOrder();
}
