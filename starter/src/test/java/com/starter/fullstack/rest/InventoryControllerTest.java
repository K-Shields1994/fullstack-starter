// TASK 2: Created a new Java file to
package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  //FIXED
  private static final String INVENTORY_ID = "ID";
  private static final String INVENTORY_NAME = "TEST";
  private static final String URL_TEMPLATE = "/inventory";
  private static final String OTHER_INV_ID = "OTHER ID";
  private static final String ALSO_INV_NAME = "ALSO TEST";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId(INVENTORY_ID); //FIXED
    this.inventory.setName(INVENTORY_NAME); //FIXED
    // Sets the Mongo ID for us
    this.inventory = this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test findAll endpoint.
   *
   * @throws Throwable see MockMvc
   */
  @Test
  public void findAll() throws Throwable {
    this.mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE) //FIXED
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().json("[" + this.objectMapper.writeValueAsString(inventory) + "]"));
  }

  /**
   * Test create endpoint.
   *
   * @throws Throwable see MockMvc
   */
  @Test
  public void create() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId(OTHER_INV_ID); //FIXED
    this.inventory.setName(ALSO_INV_NAME); //FIXED
    this.mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
      .andExpect(MockMvcResultMatchers.status().isOk());

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }

  /**
   * Test delete endpoint.
   *
   * @throws Throwable see MockMvc
   */
  @Test
  public void deleteInventory() throws Throwable {
    this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + this.inventory.getId())
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().json(this.objectMapper.writeValueAsString(this.inventory)));

    Assert.assertEquals(0, this.mongoTemplate.findAll(Inventory.class).size());
  }

}