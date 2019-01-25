package com.devinkin.jpa.test;

import com.devinkin.jpa.helloworld.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

public class JPATest {
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

    // 类似于hibernate中Session的get方法
    @Test
    public void testFind() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println("------------------------------------------");
        System.out.println(customer);
    }

    // 类似于hibernate中的load方法
    @Test
    public void testGetReference() {
        Customer customer = entityManager.getReference(Customer.class, 1);
        System.out.println(customer.getClass().getName());
        System.out.println("------------------------------------------");
        System.out.println(customer);
    }

    // 类似于hibernate的save方法，使对象由临时状态变为持久化状态
    @Test
    public void testPersistence() {
        Customer customer = new Customer();
        customer.setAge(15);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("devinkin@163.com");
        customer.setLastName("lala");
        customer.setId(100);
        entityManager.persist(customer);

        System.out.println(customer.getId());
    }

    // 类似于hibernate的delete方法，把对象的记录从数据库中移除
    // 该方法只能移除持久化对象，而hibernate的delete方法实际上还可以移除游离对象
    @Test
    public void testRemove() {
//        Customer customer = new Customer();
//        customer.setId(2);
        Customer customer = entityManager.find(Customer.class, 2);
        entityManager.remove(customer);
    }

    // 类似于hibernate Session的saveOrUpdate方法。
    @Test
    public void testMerge1() {
        // 1. 若传入的是临时对象
        // 会创建一个新的对象，吧临时对象的属性复制到新的对象中，然后对新的对象执行持久化操作
        // 新的对象中有id，但以前的临时对象中没有id
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("cc@163.com");
        customer.setLastName("CC");

        Customer customer1 = entityManager.merge(customer);
        System.out.println("customer#id: " + customer.getId());
        System.out.println("customer1#id: " + customer1.getId());
    }

    // 若传入的是一个游离对象，即传入的对象由OID。
    // 1. 若在EntityManager缓存中没有该对象
    // 2. 若在数据库中也没有对应的记录
    // 3. JPA会创建一个新的对象，然后把当前游离对象的属性复制到新创建的对象中
    // 4. 对新创建的对象执行insert操作
    @Test
    public void testMerge2() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("dd@163.com");
        customer.setLastName("DD");

        customer.setId(100);

        Customer customer1 = entityManager.merge(customer);
        System.out.println("customer#id: " + customer.getId());
        System.out.println("customer2#id: " + customer1.getId());
    }

    // 若传入的是一个游离对象，即传入的对象由OID。
    // 1. 若在EntityManager缓存中没有该对象
    // 2. 若在数据库中有对应的记录
    // 3. JPA会查询对应的记录，然后返回该记录对应的对象，然后会把游离对象的属性复制到查询到的持久化对象中
    // 4. 对查询得到的持久化象执行update操作
    @Test
    public void testMerge3() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("ee@163.com");
        customer.setLastName("EE");

        customer.setId(4);

        Customer customer1 = entityManager.merge(customer);
        System.out.println(customer == customer1);
    }

    // 若传入的是一个游离对象，即传入的对象由OID。
    // 1. 若在EntityManager缓存中有该对象
    // 2. JPA会把游离对象的属性复制到EntityManager缓存中的对象。
    // 3. EntityManager缓存中的对象执行UPDATE
    @Test
    public void testMerge4() {
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("ff@163.com");
        customer.setLastName("FF");

        customer.setId(4);

        Customer customer1 = entityManager.find(Customer.class, 4);
        entityManager.merge(customer);

        // false
        System.out.println(customer == customer1);
    }

    // 和hibernate中Session的flush方法类似
    @Test
    public void testFlush() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(customer);

        customer.setLastName("aa");

        entityManager.flush();
    }

    // 和hibernate中Session的refresh方法类似
    @Test
    public void testRefresh() {
        Customer customer = entityManager.find(Customer.class, 1);
        customer = entityManager.find(Customer.class, 1);

        entityManager.refresh(customer);
    }

    @After
    public void destory() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
