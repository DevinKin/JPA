package com.devinkin.jpa.helloworld;

import javax.persistence.*;

@Table(name = "JPA_ORDERS")
@Entity
public class Order2 {
    private Integer id;
    private String orderName;


    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
