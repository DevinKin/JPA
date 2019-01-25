package com.devinkin.jpa.test;

import com.devinkin.jpa.helloworld.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.Date;

public class JPATestMapping {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void initial() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    /**
     * 保存多对一时，建议先保存1的一端，后保存n的一端，这样不会多出额外的UPDATE语句。
     * 若是双向一对多的关联关系，执行保存时
     * 若先保存n的一端，再保存1的一端，会多出4条update语句
     */
    @Test
    public void testManyToOnePersist() {
        Customer customer = new Customer();
        customer.setAge(15);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("hh@163.com");
        customer.setLastName("GG");

        Order order1 = new Order();
        order1.setOrderName("H-HH-1");
        Order order2 = new Order();
        order2.setOrderName("H-HH-2");
        Order order3 = new Order();
        order3.setOrderName("H-HH-3");
        customer.getOrders().add(order1);
        customer.getOrders().add(order2);
        customer.getOrders().add(order3);

        // 设置关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);
        order3.setCustomer(customer);

        // 执行保存操作
//        entityManager.persist(customer);
        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
    }


    // 默认情况下，使用左外连接的方式获取n的一端的对象和其关联的1的一端的对象
    // 可以使用@ManyToOne的fetch属性，来修改默认的关联属性的加载策略
    @Test
    public void testManyToOneFind() {
        Order order = entityManager.find(Order.class, 1);
        System.out.println(order.getOrderName());
        System.out.println(order.getCustomer().getLastName());
    }

    @Test
    public void testManyToOneRemove() {
        // 删除n端
//        Order order = entityManager.find(Order.class, 1);
//        entityManager.remove(order);
        // 不能直接删除1都端，因为有外键约束
        Customer customer = entityManager.find(Customer.class, 7);
        entityManager.remove(customer);
    }

    @Test
    public void testManyToOneUpdate() {
        Order order = entityManager.find(Order.class, 2);
        order.getCustomer().setLastName("FFF");
    }

    // 单向1-n关联关系执行保存时，一定会多出update语句。
    // 因为n一端在插入时不会同时插入外键列
//    @Test
//    public void testOneToManyPersist() {
//        Customer customer = new Customer();
//        customer.setAge(15);
//        customer.setBirth(new Date());
//        customer.setCreatedTime(new Date());
//        customer.setEmail("oo@163.com");
//        customer.setLastName("OO");
//
//        Order2 order1 = new Order2();
//        order1.setOrderName("O-OO-1");
//        Order2 order2 = new Order2();
//        order2.setOrderName("O-OO-2");
//        Order2 order3 = new Order2();
//        order3.setOrderName("O-OO-3");
//
//        // 建立挂链关系
//        customer.getOrders().add(order1);
//        customer.getOrders().add(order2);
//        customer.getOrders().add(order3);
//
//        // 执行保存操作
//        entityManager.persist(customer);
//        entityManager.persist(order1);
//        entityManager.persist(order2);
//        entityManager.persist(order3);
//    }

    // 默认对多的乙方使用懒加载的加载策略
    @Test
    public void testOneToManyFind() {
        Customer customer = entityManager.find(Customer.class, 9);
        System.out.println(customer.getLastName());
        System.out.println(customer.getOrders().size());
    }


    // 默认情况下，若删除1的一端，则会先把关联的n的一端的外键置空，然后进行删除
    // 可以通过@OneToMany的fetch属性来修改默认的加载策略
    @Test
    public void testOneToManyRemove() {
        Customer customer = entityManager.find(Customer.class, 8);
        entityManager.remove(customer);
    }


    @Test
    public void testOneToManyUpdate() {
        Customer customer = entityManager.find(Customer.class, 10);
        customer.getOrders().iterator().next().setOrderName("O-XXX-10");
    }

    @Test
    public void testOneToOne() {
        Manager mgr = new Manager();
        mgr.setMgrName("M-BB");

        Department dept = new Department();
        dept.setDeptName("D-BB");

        // 设置关联关系
        mgr.setDept(dept);
        dept.setManager(mgr);

        // 执行保存操作
        entityManager.persist(dept);
        entityManager.persist(mgr);
    }

    @Test
    public void testOneToOneFind() {
//        Department dept = entityManager.find(Department.class, 1);
//        System.out.println(dept.getDeptName());
//        System.out.println(dept.getManager().getClass().getName());
        Manager mgr = entityManager.find(Manager.class, 1);
        System.out.println(mgr.getMgrName());
        System.out.println(mgr.getDept().getClass().getName());
    }


    @Test
    public void testManyToManyPersist() {
        Item i1 = new Item();
        i1.setItemName("i-1");
        Item i2 = new Item();
        i2.setItemName("i-2");

        Category c1 = new Category();
        c1.setCategoryName("c-1");
        Category c2 = new Category();
        c2.setCategoryName("c-2");

        // 设置关联关系
        i1.getCategories().add(c1);
        i2.getCategories().add(c2);

        i1.getCategories().add(c1);
        i2.getCategories().add(c2);

        c1.getItems().add(i1);
        c2.getItems().add(i2);

        c1.getItems().add(i2);
        c2.getItems().add(i1);

        // 执行保存
        entityManager.persist(i1);
        entityManager.persist(i2);
        entityManager.persist(c1);
        entityManager.persist(c2);
    }


    @Test
    public void testManyToManyFind() {
//        Item item = entityManager.find(Item.class, 1);
//        System.out.println(item.getItemName());
//        System.out.println(item.getCategories().size());

        Category category = entityManager.find(Category.class, 1);
        System.out.println(category.getCategoryName());
        System.out.println(category.getItems().size());
    }

    @After
    public void destory() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
