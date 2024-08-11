package com.simocaccia.awsomepizza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.repository.PizzaOrderRepository;
import com.simocaccia.awsomepizza.request.PizzaOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PizzaControllerTestIT {

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidRequest_whenPostOrder_thenReturnsValidResponse() throws Exception {
        PizzaOrderRequest request = new PizzaOrderRequest(List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA));

        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderList[0]").value("MARGHERITA"))
                .andExpect(jsonPath("$.orderList[1]").value("ORTOLANA"))
                .andDo(print());

    }
    @Test
    void givenValidRequest_whenPostOrder_thenAddsRowInPizzaOrderTable() throws Exception {
        PizzaOrderRequest request = new PizzaOrderRequest(List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA));

        long ordersAmountBefore = pizzaOrderRepository.count();

        mockMvc.perform(post("/api/v1/order")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        long ordersAmountAfter = pizzaOrderRepository.count();

        assertEquals(ordersAmountBefore+1, ordersAmountAfter);

    }

    @Test
    void givenValidRequestAndExistingOrder_whenGetOrderStatus_thenReturnsValidResponse() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(get("/api/v1/order/{orderId}/status", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andDo(print());
    }
    @Test
    @Rollback
    void givenValidRequestAndNonExistingOrder_whenGetOrderStatus_thenReturnsBadRequestStatus() throws Exception {
        Long orderId = 1L;

        pizzaOrderRepository.deleteById(orderId);

        mockMvc.perform(get("/api/v1/order/{orderId}/status", orderId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void givenValidRequest_whenOrderComplete_thenReturnsValidResponse() throws Exception {
        mockMvc.perform(get("/api/v1/order/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completedOrderId").exists())
                .andExpect(jsonPath("$.startedOrderId").exists())
                .andDo(print());
    }

}
