package com.simocaccia.awsomepizza.service;

public record PizzaOrderCompleteDTO(
        Long completedOrderId,
        Long startedOrderId
) {
    public static PizzaOrderCompleteDTO withCompletedAndStartedOrderId(Long completedOrderId, Long startedOrderId) {
        return new PizzaOrderCompleteDTO(completedOrderId, startedOrderId);
    }
}
