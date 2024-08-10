package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PizzaOrderCommandBasic implements PizzaOrderCommand {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaOrderCommandBasic(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Override
    public Long run(PizzaOrderRequest request) {
        return pizzaService.takeOrder(request.pizzaList());
    }
}