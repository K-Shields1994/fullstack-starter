package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;


/**
 * Test Inventory DAO.
 */
@DataMongoTest
@RunWith(SpringRunner.class)
public class InventoryDAOTest {
  @ClassRule
  public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  @Resource
  private MongoTemplate mongoTemplate;
  private InventoryDAO inventoryDAO;
  private static final String NAME = "Amber";
  private static final String PRODUCT_TYPE = "hops";

  @Before
  public void setup() {
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
  }

  @After
  public void tearDown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test Find All method.
   */
  @Test
  public void findAll() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    this.mongoTemplate.save(inventory);
    List<Inventory> actualInventory = this.inventoryDAO.findAll();
    Assert.assertFalse(actualInventory.isEmpty());
  }

  @Test
  public void create() {
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    Inventory createdInventory = this.inventoryDAO.create(inventory);

    Assert.assertNotNull(createdInventory.getId());
    Assert.assertEquals(NAME, createdInventory.getName());
    Assert.assertEquals(PRODUCT_TYPE, createdInventory.getProductType());

    Optional<Inventory> retrievedInventory = Optional.ofNullable(this.mongoTemplate.findById(createdInventory.getId(), Inventory.class));
    Assert.assertTrue(retrievedInventory.isPresent());
    Assert.assertEquals(NAME, retrievedInventory.get().getName());
    Assert.assertEquals(PRODUCT_TYPE, retrievedInventory.get().getProductType());
  }

  @Test
  public void delete(){
    Inventory inventory = new Inventory();
    inventory.setName(NAME);
    inventory.setProductType(PRODUCT_TYPE);
    inventory = this.mongoTemplate.save(inventory);

    Optional<Inventory> deletedInventory = this.inventoryDAO.delete(inventory.getId());
    Assert.assertTrue(deletedInventory.isPresent());
    Assert.assertEquals(inventory.getId(), deletedInventory.get().getId());

    Optional<Inventory> retrievedInventory = this.inventoryDAO.retrieve(inventory.getId());
    Assert.assertFalse(retrievedInventory.isPresent());
  }
}
