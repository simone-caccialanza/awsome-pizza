package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrder;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PizzaOrderGetAllCommand {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaOrderGetAllCommand(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    public List<PizzaOrder> run() {
        return pizzaService.getAllOrders();
    }
}