package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.request.PizzaOrderRequest;


public interface PizzaOrderCommand {
    Long run(PizzaOrderRequest request);
}
