package com.simocaccia.awsomepizza.controller;

import com.simocaccia.awsomepizza.command.PizzaOrderCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderCompleteCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderStatusCommand;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.response.PizzaOrderCompleteResponse;
import com.simocaccia.awsomepizza.response.PizzaOrderResponse;
import com.simocaccia.awsomepizza.response.PizzaOrderStatusReponse;
import com.simocaccia.awsomepizza.service.PizzaOrderCompleteDTO;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class PizzaController {

    private final PizzaOrderCommand pizzaOrderCommand;

    private final PizzaOrderStatusCommand pizzaOrderStatusCommand;

    private final PizzaOrderCompleteCommand pizzaOrderCompleteCommand;

    @Autowired
    public PizzaController(PizzaOrderCommand pizzaOrderCommand, PizzaOrderStatusCommand pizzaOrderStatusCommand, PizzaOrderCompleteCommand pizzaOrderCompleteCommand) {
        this.pizzaOrderCommand = pizzaOrderCommand;
        this.pizzaOrderStatusCommand = pizzaOrderStatusCommand;
        this.pizzaOrderCompleteCommand = pizzaOrderCompleteCommand;
    }


    @PostMapping("/order")
    public ResponseEntity<PizzaOrderResponse> order(
            @RequestBody @Validated @NotNull PizzaOrderRequest request
    ) {

        log.info("New order received: {}", request.pizzaList());
        log.debug("Order payload: {}", request);
        Long id = pizzaOrderCommand.run(request);
        return ResponseEntity.accepted()
                .body(PizzaOrderResponse.builder()
                        .orderId(id)
                        .orderList(request.pizzaList())
                        .build()
                );
    }

    @GetMapping("/order/{orderId}/status")
    public ResponseEntity<PizzaOrderStatusReponse> orderStatus(
            @PathVariable Long orderId
    ) {
        log.info("Received request to check order status for [{}]", orderId);
        PizzaOrderStatus status = pizzaOrderStatusCommand.run(orderId);
        return ResponseEntity.ok(
                PizzaOrderStatusReponse.builder()
                        .status(status)
                        .build()
        );
    }

    @GetMapping("order/complete")
    public ResponseEntity<PizzaOrderCompleteResponse> orderComplete() {
        log.info("Received request to complete an order and start a new one");
        PizzaOrderCompleteDTO result = pizzaOrderCompleteCommand.run();
        return ResponseEntity.ok()
                .body(PizzaOrderCompleteResponse.builder()
                        .completedOrderId(result.completedOrderId())
                        .startedOrderId(result.startedOrderId())
                        .build());
    }
}
