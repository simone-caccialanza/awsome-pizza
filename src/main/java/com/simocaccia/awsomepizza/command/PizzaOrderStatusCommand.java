package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;


public interface PizzaOrderStatusCommand {
    PizzaOrderStatus run(Long orderId);
}
