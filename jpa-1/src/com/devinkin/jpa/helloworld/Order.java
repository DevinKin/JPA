package com.devinkin.jpa.helloworld;

import javax.persistence.*;

@Table(name = "JPA_ORDERS")
@Entity
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

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

    // 映射单向n-1的关联关系
    // 使用@ManyToOne来映射多对一的关联关系
    // 使用@JoinColumn来映射外键
    // 可以使用@ManyToOne的fetch属性，来修改默认的关联属性的加载策略
    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
