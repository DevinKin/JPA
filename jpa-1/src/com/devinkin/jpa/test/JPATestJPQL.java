package com.devinkin.jpa.test;

import com.devinkin.jpa.helloworld.Customer;
import com.devinkin.jpa.helloworld.Order;
import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

public class JPATestJPQL {
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

    @Test
    public void testHelloJPQL() {
        String jpql = "FROM Customer c WHERE c.age > ?";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,15);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());
    }

    @Test
    public void testPartlyProperties() {
        String jpql = "SELECT new Customer(c.lastName, c.age) FROM Customer c WHERE c.id > ?";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1, 1);
        List resultList = query.getResultList();
        System.out.println(resultList.size());
    }

    @Test
    public void testNamedQuery() {
        Query query = entityManager.createNamedQuery("testNamedQuery");
        query.setParameter(1, 3);
        Customer customer = (Customer) query.getSingleResult();
        System.out.println(customer);
    }

    @Test
    public void testNativeQuery() {
        String sql = "SELECT age FROM JPA_CUSTOMERS WHERE id = ?";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter(1,2);
        Integer age = (Integer) nativeQuery.getSingleResult();
        System.out.println(age);
    }


    @Test
    public void testQueryCache() {
        String jpql = "FROM Customer c WHERE c.age > ?";
        Query query = entityManager.createQuery(jpql)
                .setHint(QueryHints.HINT_CACHEABLE,true);
        query.setParameter(1,1);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());

        query.setParameter(1,1)
                .setHint(QueryHints.HINT_CACHEABLE,true);
        customers = query.getResultList();
        System.out.println(customers.size());
    }



    @Test
    public void testOrderBy() {
        String jpql = "FROM Customer c WHERE c.age > ? ORDER BY c.age DESC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,1);
        List<Customer> customers = query.getResultList();
        System.out.println(customers.size());
    }

    @Test
    public void testGroupBy() {
        String jpql = "SELECT o.customer FROM Order o GROUP BY o.id HAVING o.id > ?";
        Query query = entityManager.createQuery(jpql);

        query.setParameter(1,2);
        List<Customer> customers = query.getResultList();
        System.out.println(customers);
        System.out.println(customers.size());
    }


    @Test
    public void testLeftOuterJoinFetch() {
//        String jpql = "FROM Customer c LEFT OUTER JOIN FETCH c.orders WHERE c.id = ? ";
        String jpql = "FROM Customer c LEFT OUTER JOIN c.orders WHERE c.id = ? ";
        Query query= entityManager.createQuery(jpql);
        query.setParameter(1,12);
//        Customer customer = (Customer) query.getSingleResult();
//        System.out.println(customer.getLastName());
//        System.out.println(customer.getOrders().size());

        List<Object[]> result = query.getResultList();
        System.out.println(result);
    }


    @Test
    public void testSubQuery() {
        // 查询所有Customer的lastName为YY的Order
        String jpql = "SELECT o FROM Order o " +
                "WHERE o.customer = (SELECT c FROM Customer c WHERE c.lastName = ?)";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1, "OO");
        List<Order> orders = query.getResultList();
        System.out.println(orders.size());
    }

    @Test
    public void testJpqlFunction() {
        String jpql = "SELECT upper(c.email) FROM Customer c";
        Query query = entityManager.createQuery(jpql);
        List<String> emails = query.getResultList();
        System.out.println(emails);
    }

    @Test
    public void testExecuteUpdate() {
        String jpql = "UPDATE Customer c SET c.lastName = ? WHERE c.id = ?";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,"YYY");
        query.setParameter(2, 12);
        query.executeUpdate();
    }

    @Test
    public void testExecuteDelete() {
        String jpql = "DELETE FROM Customer c WHERE c.id = ?";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1,13);
        query.executeUpdate();
    }

    @After
    public void destory() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
