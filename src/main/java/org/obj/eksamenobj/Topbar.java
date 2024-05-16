// Topbar.java
package org.obj.eksamenobj;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import java.io.PrintWriter;

public class Topbar extends HBox {
    private SearchHandler searchHandler;

    public Topbar(PrintWriter out, InventoryView inventoryView) {
        this.searchHandler = new SearchHandler(out, inventoryView);

        TextField searchField = new TextField("");
        Button searchButton = new Button("Søk");

        setBackground(new Background(new BackgroundFill(Color.web("#474E68"), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(10, 50, 10, 20));
        searchField.setPrefWidth(300);
        searchField.setPromptText("Søk etter produkt");
        searchButton.setStyle("-fx-background-color: #6B728E; -fx-text-fill: white;");

        searchButton.setOnAction(e -> searchHandler.performSearch(searchField.getText()));

        getChildren().addAll(searchField, searchButton);
    }
}
