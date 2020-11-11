package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    Item i = getTestItem();

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        when(itemRepository.findAll()).thenReturn(getAllTestItems());
        when(itemRepository.findByName(i.getName())).thenReturn(getAllTestItems());
        when(itemRepository.findById(i.getId())).thenReturn(Optional.ofNullable(i));
    }

    @Test
    public void get_items_test() {

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
    }

    @Test
    public void get_item_by_id_test() {

        final ResponseEntity<Item> response = itemController.getItemById(i.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();

        assertNotNull(item);
        assertEquals(i.getId(), item.getId());
    }

    @Test
    public void get_items_by_name_test() {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(i.getName());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
    }

    private Item getTestItem() {
        Item i = new Item();
        i.setId(1L);
        i.setDescription("square");
        i.setName("Needle");
        i.setPrice(BigDecimal.valueOf(1.99d));

        return i;
    }

    private List<Item> getAllTestItems() {
        return Arrays.asList(getTestItem());
    }

}
