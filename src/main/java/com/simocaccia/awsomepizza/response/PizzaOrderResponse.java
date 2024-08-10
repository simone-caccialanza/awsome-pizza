package com.simocaccia.awsomepizza.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simocaccia.awsomepizza.entity.PizzaName;
import lombok.Builder;

import java.util.Collection;

@Builder
public class PizzaOrderResponse {
    @JsonProperty
    Long orderId;
    @JsonProperty
    Collection<PizzaName> orderList;
}
