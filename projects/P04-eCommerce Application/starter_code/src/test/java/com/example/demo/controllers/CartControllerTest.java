package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    User u = getTestUser();
    Item i = getTestItem();

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        when(itemRepository.findById(i.getId())).thenReturn(Optional.of(i));
    }

    @Test
    public void add_to_cart_test() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(u.getUsername());
        r.setQuantity(1);
        r.setItemId(i.getId());

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertNotNull(cart.getUser());
        assertNotNull(cart.getItems());
        assertFalse(cart.getItems().isEmpty());
    }

    @Test
    public void remove_from_cart_test(){

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername(u.getUsername());
        r.setQuantity(1);
        r.setItemId(i.getId());

        final ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertNotNull(cart.getUser());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
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

    private Item getTestItem() {
        Item i = new Item();
        i.setId(1L);
        i.setDescription("square");
        i.setName("Needle");
        i.setPrice(BigDecimal.valueOf(1.99d));

        return i;
    }

}
