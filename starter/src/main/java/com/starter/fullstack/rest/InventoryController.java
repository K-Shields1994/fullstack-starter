package com.starter.fullstack.rest;

import java.util.List;
import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.dao.InventoryDAO;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory Controller.
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
  private final InventoryDAO inventoryDAO;

  /**
   * Default Constructor.
   * @param inventoryDAO inventoryDAO.
   */
  public InventoryController(InventoryDAO inventoryDAO) {
    Assert.notNull(inventoryDAO, "Inventory DAO must not be null.");
    this.inventoryDAO = inventoryDAO;
  }

  /**
   * Find Products.
   * @return List of Product.
   */
  @GetMapping
  public List<Inventory> findInventories() {
    return this.inventoryDAO.findAll();
  }

  /**
   * Create Inventory for PostMapping
   * @param inventory Create Inventory
   * @return Created Inventory
   */

  // TASK 2
  @PostMapping
  public Inventory create(@RequestBody Inventory inventory) {
    return this.inventoryDAO.create(inventory);
  }

  /**
   * Delete Inventory by ID.
   * @param id ID of Inventory to Delete.
   * @return Deleted Inventory.
   */

  @DeleteMapping("/{id}")
  public Inventory delete(@PathVariable String id) {
    return this.inventoryDAO.delete(id).orElse(null);
  }

}

