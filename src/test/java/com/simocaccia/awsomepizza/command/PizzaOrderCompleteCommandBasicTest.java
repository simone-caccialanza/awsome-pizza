package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.service.PizzaOrderCompleteDTO;
import com.simocaccia.awsomepizza.service.PizzaService;
import com.simocaccia.awsomepizza.util.AcceptedOrderNotFoundException;
import com.simocaccia.awsomepizza.util.StartedOrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PizzaOrderCompleteCommandBasicTest {

    @Mock
    private PizzaService pizzaService;

    @InjectMocks
    private PizzaOrderCompleteCommandBasic pizzaOrderCompleteCommandBasic;


    @Test
    void givenExistingAcceptedAndStartedOrder_whenRun_thenCompletesAndStartsOrders() {
        Long expectedCompletedOrderId = 123L;
        Long expectedStartedOrderId = 456L;
        when(pizzaService.completeStartedOrder()).thenReturn(expectedCompletedOrderId);
        when(pizzaService.startAcceptedOrder()).thenReturn(expectedStartedOrderId);

        PizzaOrderCompleteDTO result = pizzaOrderCompleteCommandBasic.run();

        verify(pizzaService).completeStartedOrder();
        verify(pizzaService).startAcceptedOrder();

        assertEquals(expectedCompletedOrderId, result.completedOrderId());
        assertEquals(expectedStartedOrderId, result.startedOrderId());
    }

    @Test
    void testRun_NoStartedOrderToComplete() {
        Long expectedStartedOrderId = 123L;
        when(pizzaService.completeStartedOrder()).thenThrow(new StartedOrderNotFoundException("No started order found"));
        when(pizzaService.startAcceptedOrder()).thenReturn(expectedStartedOrderId);

        PizzaOrderCompleteDTO commandResult = pizzaOrderCompleteCommandBasic.run();

        verify(pizzaService).completeStartedOrder();
        verify(pizzaService).startAcceptedOrder();

        assertNull(commandResult.completedOrderId());
        assertEquals(expectedStartedOrderId, commandResult.startedOrderId());
    }

    @Test
    void testRun_NoAcceptedOrderToStart() {
        Long expectedCompletedOrderId = 123L;
        when(pizzaService.completeStartedOrder()).thenReturn(expectedCompletedOrderId);
        when(pizzaService.startAcceptedOrder()).thenThrow(new AcceptedOrderNotFoundException("No accepted order found"));

        PizzaOrderCompleteDTO commandResult = pizzaOrderCompleteCommandBasic.run();

        verify(pizzaService).completeStartedOrder();
        verify(pizzaService).startAcceptedOrder();

        assertEquals(expectedCompletedOrderId, commandResult.completedOrderId());
        assertNull(commandResult.startedOrderId());
    }

    @Test
    void testRun_NoOrdersToCompleteOrStart() {
        when(pizzaService.completeStartedOrder()).thenThrow(new StartedOrderNotFoundException("No started order found"));
        when(pizzaService.startAcceptedOrder()).thenThrow(new AcceptedOrderNotFoundException("No accepted order found"));

        PizzaOrderCompleteDTO commandResult = pizzaOrderCompleteCommandBasic.run();

        verify(pizzaService).completeStartedOrder();
        verify(pizzaService).startAcceptedOrder();

        assertNull(commandResult.completedOrderId());
        assertNull(commandResult.startedOrderId());
    }
}
