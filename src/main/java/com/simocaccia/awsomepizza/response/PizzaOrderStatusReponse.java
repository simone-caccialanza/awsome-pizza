package com.simocaccia.awsomepizza.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import lombok.Builder;

@Builder
public class PizzaOrderStatusReponse {
    @JsonProperty
    private PizzaOrderStatus status;
}
