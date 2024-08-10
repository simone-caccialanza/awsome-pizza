package com.simocaccia.awsomepizza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simocaccia.awsomepizza.command.PizzaOrderCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderCompleteCommand;
import com.simocaccia.awsomepizza.command.PizzaOrderStatusCommand;
import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import com.simocaccia.awsomepizza.service.PizzaOrderCompleteDTO;
import com.simocaccia.awsomepizza.util.CheckOrderNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PizzaControllerTest {

    private static String orderResponse;
    @MockBean
    private PizzaOrderCommand pizzaOrderCommand;
    @MockBean
    private PizzaOrderStatusCommand pizzaOrderStatusCommand;
    @MockBean
    private PizzaOrderCompleteCommand pizzaOrderCompleteCommand;
    @Autowired
    private PizzaController pizzaController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        orderResponse = loadFile("expected_order_response.json");
    }

    private static String loadFile(String classPath) {
        try {
            return new String(Files.readAllBytes(Paths.get(new ClassPathResource(classPath).getURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> testOrderCompleteData() {
        return Stream.of(
                Arguments.of(123L, 456L),
                Arguments.of(null, 456L),
                Arguments.of(123L, null),
                Arguments.of(null, null)
        );
    }


    @Test
    void testOrder_shouldReturnAcceptedResponse() throws Exception {
        Long orderId = 123L;
        PizzaOrderRequest request = new PizzaOrderRequest(List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA));
        when(pizzaOrderCommand.run(request)).thenReturn(orderId);


        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(orderResponse));
    }

    @Test
    void testOrderStatus_shouldReturnValidStatus() throws Exception {
        Long orderId = 123L;
        PizzaOrderStatus status = PizzaOrderStatus.STARTED;
        when(pizzaOrderStatusCommand.run(orderId)).thenReturn(status);

        mockMvc.perform(get("/api/v1/order/{orderId}/status", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.toString()));
    }

    @Test
    void testOrderStatus_shouldHandleCheckOrderNotFoundException() throws Exception {
        Long orderId = 123L;
        when(pizzaOrderStatusCommand.run(orderId)).thenThrow(new CheckOrderNotFoundException("Order with id: " + orderId + " not found"));

        mockMvc.perform(get("/api/v1/order/{orderId}/status", orderId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Order with id: " + orderId + " not found"))
                .andExpect(jsonPath("$.instance").value("/api/v1/order/" + orderId + "/status"));
    }

    @ParameterizedTest
    @MethodSource("testOrderCompleteData")
    void testOrderComplete_shouldReturnValidResponse(Long completedOrderId, Long startedOrderId) throws Exception {
        PizzaOrderCompleteDTO pizzaOrderCompleteDTO = new PizzaOrderCompleteDTO(completedOrderId, startedOrderId);
        when(pizzaOrderCompleteCommand.run()).thenReturn(pizzaOrderCompleteDTO);

        mockMvc.perform(get("/api/v1/order/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedOrderId").value(completedOrderId))
                .andExpect(jsonPath("$.startedOrderId").value(startedOrderId));
    }
}
