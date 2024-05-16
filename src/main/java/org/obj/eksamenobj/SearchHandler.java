// SearchHandler.java
package org.obj.eksamenobj;

import java.io.PrintWriter;

public class SearchHandler {
    private PrintWriter out;
    private InventoryView inventoryView;

    public SearchHandler(PrintWriter out, InventoryView inventoryView) {
        this.out = out;
        this.inventoryView = inventoryView;
    }

    public void performSearch(String searchText) {
        if (out != null) {
            String[] searchTerms = searchText.split(" ");
            String searchQuery = String.join(";", searchTerms);
            out.println("SEARCH;" + searchQuery);
        }
    }
}
