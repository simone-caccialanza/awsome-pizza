package com.simocaccia.awsomepizza.request;

import com.simocaccia.awsomepizza.entity.PizzaName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

public record PizzaOrderRequest(
        @NotEmpty(message = "pizzaList must not be empty")
        Collection<@NotNull(message = "pizza name must not be null") PizzaName> pizzaList
) {
}
