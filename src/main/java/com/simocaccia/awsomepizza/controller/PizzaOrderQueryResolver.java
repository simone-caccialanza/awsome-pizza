package com.simocaccia.awsomepizza.controller;

import com.simocaccia.awsomepizza.command.PizzaOrderCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderGetAllCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderStatusCommand;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.response.PizzaOrderResponse;
import com.simocaccia.awsomepizza.response.PizzaOrderStatusReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PizzaOrderQueryResolver {

    private final PizzaOrderGetAllCommand pizzaOrderGetAllCommand;
    private final PizzaOrderStatusCommand pizzaOrderStatusCommand;
    private final PizzaOrderCommand pizzaOrderCommand;

    @Autowired
    public PizzaOrderQueryResolver(PizzaOrderGetAllCommand pizzaOrderGetAllCommand, PizzaOrderStatusCommand pizzaOrderStatusCommand, PizzaOrderCommand pizzaOrderCommand) {
        this.pizzaOrderGetAllCommand = pizzaOrderGetAllCommand;
        this.pizzaOrderStatusCommand = pizzaOrderStatusCommand;
        this.pizzaOrderCommand = pizzaOrderCommand;
    }

    @QueryMapping
    public List<PizzaOrderResponse> orders() {
        return pizzaOrderGetAllCommand.run().stream().map(order ->
            PizzaOrderResponse.builder()
                .orderId(order.getOrderId())
                .orderList(order.getItems())
                .build()
        ).toList();
    }

    @QueryMapping
    public PizzaOrderStatusReponse orderStatus(Long orderId) {
        var status = pizzaOrderStatusCommand.run(orderId);
        return PizzaOrderStatusReponse.builder().status(status).build();
    }

    @MutationMapping
    public PizzaOrderResponse addOrder(@Argument PizzaOrderRequest order) {
        Long orderId = pizzaOrderCommand.run(order);
        return PizzaOrderResponse.builder()
                .orderId(orderId)
                .orderList(order.pizzaList())
                .build();
    }
}
