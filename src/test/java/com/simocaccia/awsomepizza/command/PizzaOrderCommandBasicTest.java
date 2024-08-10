package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaOrderCommandBasicTest {

    @Mock
    private PizzaService pizzaService;

    @InjectMocks
    private PizzaOrderCommandBasic pizzaCommand;


    @Test
    void run() {
        Long expectedOrderId = 123L;
        //the request is @NonNull, the application would respond 400
        PizzaOrderRequest request = new PizzaOrderRequest(List.of(PizzaName.MARGHERITA));

        when(pizzaService.takeOrder(any())).thenReturn(expectedOrderId);

        Long result = pizzaCommand.run(request);

        verify(pizzaService).takeOrder(request.pizzaList());

        assertEquals(expectedOrderId, result);
    }
}