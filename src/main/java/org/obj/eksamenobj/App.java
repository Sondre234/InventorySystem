package org.obj.eksamenobj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * hovedklassen for applikasjonen som utvider
 */

public class App extends Application {
    private Topbar topBar;
    private InventoryView inventoryView;
    private Botbar botBar;
    private ServerCommunication serverCommunication;

    /**
     * starter applikasjonen
     *
     * @param stage hovedscenen for applikasjonen
     */

    @Override
    public void start(Stage stage) {
        inventoryView = new InventoryView();
        serverCommunication = new ServerCommunication(inventoryView);

        topBar = new Topbar(serverCommunication.getOut(), inventoryView);
        botBar = new Botbar();

        BorderPane bp = new BorderPane();
        bp.setTop(topBar);
        bp.setCenter(inventoryView);
        bp.setBottom(botBar);

        Scene scene = new Scene(bp, 800, 700);
        stage.setTitle("Inventory");
        stage.setScene(scene);
        stage.show();

        serverCommunication.startListening();
    }

    /**
     * stopper applikasjonen og avslutter serverkommunikasjonen
     *
     * @throws Exception hvis det oppstår en feil ved stopping
     */
    @Override
    public void stop() throws Exception {
        serverCommunication.stop();
        Platform.exit();
        System.exit(0);
    }

    /**
     * hovedmetode som starter applikasjonen
     *
     * @param args kommandolinjeargumenter
     */
    public static void main(String[] args) {
        launch();
    }
}
