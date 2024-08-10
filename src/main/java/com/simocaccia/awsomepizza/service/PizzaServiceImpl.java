package com.simocaccia.awsomepizza.service;

import com.simocaccia.awsomepizza.entity.PizzaName;
import com.simocaccia.awsomepizza.entity.PizzaOrder;
import com.simocaccia.awsomepizza.entity.PizzaOrderStatus;
import com.simocaccia.awsomepizza.repository.PizzaOrderRepository;
import com.simocaccia.awsomepizza.util.AcceptedOrderNotFoundException;
import com.simocaccia.awsomepizza.util.CheckOrderNotFoundException;
import com.simocaccia.awsomepizza.util.StartedOrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class PizzaServiceImpl implements PizzaService {

    private final PizzaOrderRepository pizzaOrderRepository;


    @Autowired
    public PizzaServiceImpl(PizzaOrderRepository pizzaOrderRepository) {
        this.pizzaOrderRepository = pizzaOrderRepository;
    }

    /**
     * @param items list of pizzas in a new order
     * @return the orderId of the order
     */
    @Override
    public Long takeOrder(Collection<PizzaName> items) {

        PizzaOrder order = new PizzaOrder();
        order.setItems(items);
        order.setStatus(PizzaOrderStatus.ACCEPTED);

        log.info("Saving new order");
        PizzaOrder savedOrder = pizzaOrderRepository.save(order);
        log.info("Order saved with id: [{}]", savedOrder.getOrderId());
        log.debug("Order saved is {}", savedOrder);

        return savedOrder.getOrderId();
    }

    /**
     * Checks the status of a pizza order given its unique identifier.
     *
     * @param id the identifier of the pizza order.
     * @return the current status of the pizza order.
     * @throws CheckOrderNotFoundException if no pizza order is found with the given identifier.
     */
    @Override
    public PizzaOrderStatus checkOrderStatus(Long id) throws CheckOrderNotFoundException {
        log.info("Checking order status for id [{}]", id);
        Optional<PizzaOrder> order = pizzaOrderRepository.findById(id);
        if (order.isPresent()) {
            log.info("Order status for id: [{}] is {}", id, order.get().getStatus());
            return order.get().getStatus();
        } else {
            throw new CheckOrderNotFoundException("Order with id: " + id + " not found");
        }
    }

    /**
     * Completes a started pizza order and updates its status to COMPLETED.
     *
     * @return the id of the completed order.
     * @throws StartedOrderNotFoundException if no started order is found.
     */
    @Override
    public Long completeStartedOrder() throws StartedOrderNotFoundException {
        log.info("Completing order");
        Optional<PizzaOrder> orderToClose = pizzaOrderRepository.findByStatus(PizzaOrderStatus.STARTED);
        if (orderToClose.isPresent()) {
            PizzaOrder order = orderToClose.get();
            order.setStatus(PizzaOrderStatus.COMPLETED);
            pizzaOrderRepository.save(order);
            log.info("Order with id [{}] has been completed", order.getOrderId());
            return order.getOrderId();
        } else {
            throw new StartedOrderNotFoundException("Started order not found");
        }
    }

    /**
     * Starts a new accepted pizza order and updates its status to STARTED.
     *
     * @return the id of the started order.
     * @throws AcceptedOrderNotFoundException if no accepted order is found.
     */
    @Override
    public Long startAcceptedOrder() throws AcceptedOrderNotFoundException {
        log.info("Starting new order");
        Optional<PizzaOrder> orderToStart = pizzaOrderRepository.findByMinIdAndStatus(PizzaOrderStatus.ACCEPTED);
        if (orderToStart.isPresent()) {
            PizzaOrder order = orderToStart.get();
            order.setStatus(PizzaOrderStatus.STARTED);
            pizzaOrderRepository.save(order);
            log.info("Order with id [{}] has started", order.getOrderId());
            return order.getOrderId();
        } else {
            throw new AcceptedOrderNotFoundException("Accepted order not found");
        }
    }


}
