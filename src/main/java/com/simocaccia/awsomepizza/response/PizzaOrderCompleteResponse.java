package com.simocaccia.awsomepizza.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class PizzaOrderCompleteResponse {
    @JsonProperty
    Long completedOrderId;
    @JsonProperty
    Long startedOrderId;
}
