package com.simocaccia.awsomepizza.command;

import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.service.PizzaService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PizzaOrderStatusCommandBasicTest {

    @Mock
    private PizzaService pizzaService;

    @InjectMocks
    private PizzaOrderStatusCommandBasic pizzaCommand;

    private static Stream<Arguments> runData() {
        return Stream.of(
                Arguments.of(123L, PizzaOrderStatus.ACCEPTED),
                Arguments.of(456L, PizzaOrderStatus.STARTED),
                Arguments.of(789L, PizzaOrderStatus.COMPLETED)
        );
    }

    @ParameterizedTest
    @MethodSource("runData")
    void run(Long orderId, PizzaOrderStatus expectedStatus) {
        when(pizzaService.checkOrderStatus(any())).thenReturn(expectedStatus);

        PizzaOrderStatus actualStatus = pizzaCommand.run(orderId);

        verify(pizzaService).checkOrderStatus(orderId);

        assertEquals(expectedStatus, actualStatus);
    }
}