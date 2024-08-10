package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.service.PizzaOrderCompleteDTO;


public interface PizzaOrderCompleteCommand {
    PizzaOrderCompleteDTO run();
}
