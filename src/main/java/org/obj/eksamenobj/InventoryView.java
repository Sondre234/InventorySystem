package org.obj.eksamenobj;

import javafx.scene.layout.VBox;

public class InventoryView extends VBox {
    private InventoryTable inventoryTable;
    private InventoryControls inventoryControls;

    public InventoryView() {
        inventoryTable = new InventoryTable();
        inventoryControls = new InventoryControls(inventoryTable);
        getChildren().addAll(inventoryControls, inventoryTable);
    }

    public InventoryTable getInventoryTable() {
        return inventoryTable;
    }
}
