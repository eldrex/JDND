package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    User u = getTestUser();
    Item i = getTestItem();

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        when(orderRepository.findByUser(u)).thenReturn(getUserOrders());
    }

    @Test
    public void get_orders_for_user() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(u.getUsername());
        r.setQuantity(1);
        r.setItemId(i.getId());

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(u.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();

        assertNotNull(userOrders);
        assertFalse(userOrders.isEmpty());
        assertEquals(1, userOrders.size());
        assertEquals(u.getUsername(), userOrders.get(0).getUser().getUsername());
    }

    private User getTestUser() {

        Cart cart = new Cart();

        User u = new User();
        u.setId(1);
        u.setUsername("test");
        u.setPassword("optional");
        u.setCart(cart);

        cart.setUser(u);

        return u;
    }

    private List<UserOrder> getUserOrders() {
        UserOrder uo = new UserOrder();
        uo.setId(1L);
        uo.setUser(u);
        uo.setItems(Arrays.asList(i));
        uo.setTotal(i.getPrice());
        return Arrays.asList(uo);
    }

    private Item getTestItem() {
        Item i = new Item();
        i.setId(1L);
        i.setDescription("square");
        i.setName("Needle");
        i.setPrice(BigDecimal.valueOf(1.99d));

        return i;
    }

}
