package com.simocaccia.awsomepizza.service;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrder;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.repository.PizzaOrderRepository;
import com.simocaccia.awsomepizza.util.AcceptedOrderNotFoundException;
import com.simocaccia.awsomepizza.util.CheckOrderNotFoundException;
import com.simocaccia.awsomepizza.util.StartedOrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceImplTest {

    @Mock
    private PizzaOrderRepository pizzaOrderRepository;

    @InjectMocks
    private PizzaServiceImpl pizzaService;


    private static Stream<Collection<PizzaName>> takeOrderInput() {
        //the application responds with 400 if this list is empty or has null items
        return Stream.of(
                List.of(PizzaName.MARGHERITA),
                List.of(PizzaName.MARGHERITA, PizzaName.MARGHERITA, PizzaName.MARGHERITA),
                List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA, PizzaName.PROSCIUTTO)
        );
    }

    @ParameterizedTest
    @MethodSource("takeOrderInput")
    void givenValidPizzaNames_whenTakeOrder_thenReturnOrderId(Collection<PizzaName> items) {
        //return the same instance that is supposed to be saved + the orderId
        when(pizzaOrderRepository.save(any(PizzaOrder.class))).thenAnswer(mock -> {
            PizzaOrder order = mock.getArgument(0);
            order.setOrderId(123L);
            return order;
        });

        Long orderId = pizzaService.takeOrder(items);

        verify(pizzaOrderRepository).save(any(PizzaOrder.class));

        assertEquals(123L, orderId);

    }

    @Test
    void givenExistingOrder_whenCheckOrderStatus_thenReturnCorrectOrderStatus() {
        Long orderId = 123L;
        PizzaOrder order = new PizzaOrder();
        order.setStatus(PizzaOrderStatus.ACCEPTED);

        when(pizzaOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        PizzaOrderStatus status = pizzaService.checkOrderStatus(orderId);

        verify(pizzaOrderRepository).findById(orderId);

        assertEquals(PizzaOrderStatus.ACCEPTED, status);
    }

    @Test
    void givenNonExistingOrder_whenCheckOrderStatus_thenThrowCheckOrderNotFoundException() {
        Long orderId = 123L;

        when(pizzaOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(CheckOrderNotFoundException.class, () -> pizzaService.checkOrderStatus(orderId));
    }

    @Test
    void givenExistingStartedOrder_whenCompleteStartedOrder_thenReturnOrderId() throws StartedOrderNotFoundException {
        Long orderId = 123L;
        PizzaOrder oldOrder = new PizzaOrder();
        oldOrder.setOrderId(orderId);
        oldOrder.setStatus(PizzaOrderStatus.STARTED);

        when(pizzaOrderRepository.findByStatus(PizzaOrderStatus.STARTED)).thenReturn(Optional.of(oldOrder));
        when(pizzaOrderRepository.save(oldOrder)).thenAnswer(mock -> {
            PizzaOrder updatedOrder = mock.getArgument(0);
            updatedOrder.setStatus(PizzaOrderStatus.COMPLETED);
            return updatedOrder;
        });

        Long completedOrderId = pizzaService.completeStartedOrder();

        verify(pizzaOrderRepository).findByStatus(PizzaOrderStatus.STARTED);
        verify(pizzaOrderRepository).save(oldOrder);

        assertEquals(orderId, completedOrderId);
    }

    @Test
    void givenNonExistingStartedOrder_whenCompleteStartedOrder_thenThrowStartedOrderNotFoundException() {

        when(pizzaOrderRepository.findByStatus(PizzaOrderStatus.STARTED)).thenReturn(Optional.empty());

        assertThrows(StartedOrderNotFoundException.class, () -> pizzaService.completeStartedOrder());
    }

    @Test
    void givenExistingAcceptedOrder_whenStartAcceptedOrder_thenReturnOrderId() throws AcceptedOrderNotFoundException {
        Long orderId = 123L;
        PizzaOrder oldOrder = new PizzaOrder();
        oldOrder.setOrderId(orderId);
        oldOrder.setStatus(PizzaOrderStatus.ACCEPTED);

        when(pizzaOrderRepository.findByMinIdAndStatus(PizzaOrderStatus.ACCEPTED)).thenReturn(Optional.of(oldOrder));

        when(pizzaOrderRepository.save(any())).thenAnswer(mock -> {
            PizzaOrder updatedOrder = mock.getArgument(0);
            updatedOrder.setStatus(PizzaOrderStatus.STARTED);
            return updatedOrder;
        });

        Long startedOrderId = pizzaService.startAcceptedOrder();

        verify(pizzaOrderRepository).findByMinIdAndStatus(PizzaOrderStatus.ACCEPTED);
        verify(pizzaOrderRepository).save(oldOrder);
        assertEquals(orderId, startedOrderId);
    }

    @Test
    void givenNonExistingAcceptedOrder_whenStartAcceptedOrder_thenThrowAcceptedOrderNotFoundException() {

        when(pizzaOrderRepository.findByMinIdAndStatus(PizzaOrderStatus.ACCEPTED)).thenReturn(Optional.empty());

        assertThrows(AcceptedOrderNotFoundException.class, () -> pizzaService.startAcceptedOrder());
    }
}