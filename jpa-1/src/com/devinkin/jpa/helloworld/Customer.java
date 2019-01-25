package com.devinkin.jpa.helloworld;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NamedQuery(name="testNamedQuery",
        query = "FROM Customer c WHERE c.id = ?")
@Cacheable
@Table(name = "JPA_CUSTOMERS")
@Entity
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    public Customer(String lastName, int age) {
        this.lastName = lastName;
        this.age = age;
    }

    private Date createdTime;

    private Set<Order> orders = new HashSet<>();

    public Customer() {
    }

    // 映射单向1-n的关联关系
    // 使用@OneToMany来映射1-n的关联关系
    // 使用JoinColumn来映射外键列的名称
    // 可以使用@OneToMany的fetch属性来修改默认的加载策略
//    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE}, mappedBy = "customer")
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "customer")
//    @JoinColumn(name = "CUSTOMER_ID")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }


    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    private Date birth;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdTime=" + createdTime +
                ", birth=" + birth +
                '}';
    }

    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @TableGenerator(name = "ID_GENERATOR",
//                    table="JPA_ID_GENERATORS",
//                    pkColumnName = "PK_NAME",
//                    pkColumnValue = "CUSTOMER_ID",
//                    valueColumnName = "PK_VALUE",
//                    allocationSize = 100)
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_GENERATOR")
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME", length = 50, nullable = true)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic(optional = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 工具方法，不需要映射为数据表的一列
    @Transient
    public String getInfo() {
        return "lastName: " + lastName + ", email: " + email;
    }
}
