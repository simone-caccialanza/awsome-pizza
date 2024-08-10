package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.service.PizzaOrderCompleteDTO;
import com.simocaccia.awsomepizza.service.PizzaService;
import com.simocaccia.awsomepizza.util.AcceptedOrderNotFoundException;
import com.simocaccia.awsomepizza.util.StartedOrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PizzaOrderCompleteCommandBasic implements PizzaOrderCompleteCommand {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaOrderCompleteCommandBasic(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Override
    public PizzaOrderCompleteDTO run() {
        Long completedOrderId;
        Long startedOrderId;
        try {
            completedOrderId = pizzaService.completeStartedOrder();
        } catch (StartedOrderNotFoundException e) {
            log.warn(e.getMessage());
            log.info("There are not started orders to complete");
            completedOrderId = null;
        }
        try {
            startedOrderId = pizzaService.startAcceptedOrder();
        } catch (AcceptedOrderNotFoundException e) {
            log.warn(e.getMessage());
            log.info("There are not accepted orders to start");
            startedOrderId = null;
        }
        return PizzaOrderCompleteDTO.withCompletedAndStartedOrderId(completedOrderId, startedOrderId);
    }
}
