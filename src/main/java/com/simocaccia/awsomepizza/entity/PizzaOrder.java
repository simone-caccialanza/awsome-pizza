package com.simocaccia.awsomepizza.entity;


import com.simocaccia.awsomepizza.util.PizzaNameConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pizza_order")
@SequenceGenerator(name = "pizza_order_seq", sequenceName = "pizza_order_seq", allocationSize = 1)
public class PizzaOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pizza_order_seq")
    @Column(name = "id", nullable = false)
    private Long orderId;

    @Column(name = "items", nullable = false)
    @Convert(converter = PizzaNameConverter.class)
    private Collection<PizzaName> items;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PizzaOrderStatus status;

    @Override
    public String toString() {
        return "PizzaOrder{" +
                "orderId=" + orderId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
