package org.obj.eksamenobj;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Botbar extends VBox {


    /**
     * denne klassen representerer bunnen av applikasjonen (botbar)
     */

    public Botbar() {

        /**
         * konstruktør for Botbar-klassen som setter bakgrunnsfargen til botbaren
         */
        setBackground(new Background(new BackgroundFill(Color.web("#474E68"), CornerRadii.EMPTY, Insets.EMPTY)));

    }


}
