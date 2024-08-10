package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PizzaOrderStatusCommandBasic implements PizzaOrderStatusCommand {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaOrderStatusCommandBasic(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Override
    public PizzaOrderStatus run(Long orderId) {
        return pizzaService.checkOrderStatus(orderId);
    }
}
