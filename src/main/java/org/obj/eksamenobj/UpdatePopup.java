package org.obj.eksamenobj;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;

public class UpdatePopup {
    private int id;
    private String[] parts;
    private PrintWriter out;
    private Stage primaryStage;

    public UpdatePopup(int id, String[] parts, PrintWriter out, Stage primaryStage) {
        this.id = id;
        this.parts = parts;
        this.out = out;
        this.primaryStage = primaryStage;
    }

    public void show() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label labelType = new Label("Inventartype:");
        TextField textFieldType = new TextField(parts[1]);
        Label labelKat = new Label("Kategori:");
        TextField textFieldKat = new TextField(parts[2]);
        Label labelBesk = new Label("Beskrivelse:");
        TextField textFieldBesk = new TextField(parts[3]);
        Label labelKjopsdato = new Label("Innkjøpsdato:");
        TextField textFieldKjopsdato = new TextField(parts[4]);
        Label labelKjopspris = new Label("Innkjøpspris:");
        TextField textFieldKjopspris = new TextField(parts[5]);
        Label labelLevetid = new Label("Forventet levetid:");
        TextField textFieldLevetid = new TextField(parts.length > 6 ? parts[6] : "");
        Label labelPlassering = new Label("Plassering:");
        TextField textFieldPlassering = new TextField(parts.length > 7 ? parts[7] : "");
        Label labelAntall = new Label("Antall:");
        TextField textFieldAntall = new TextField(parts.length > 8 ? parts[8] : "");
        Label labelTattUtAvBruk = new Label("Tatt ut av bruk:");
        CheckBox checkBoxTattUtAvBruk = new CheckBox();
        checkBoxTattUtAvBruk.setSelected(Boolean.parseBoolean(parts.length > 9 ? parts[9] : "false"));
        Label labelÅrsak = new Label("Årsak:");
        TextField textFieldÅrsak = new TextField(parts.length > 10 ? parts[10] : "");

        Button updateButton = new Button("Oppdater");
        updateButton.setOnAction(e -> {
            String type = textFieldType.getText();
            String kat = textFieldKat.getText();
            String besk = textFieldBesk.getText();
            String kjopsdato = textFieldKjopsdato.getText();
            String kjopspris = textFieldKjopspris.getText();
            String levetid = textFieldLevetid.getText();
            String plassering = textFieldPlassering.getText();
            String antall = textFieldAntall.getText();
            boolean tattUt = checkBoxTattUtAvBruk.isSelected();
            String ars = textFieldÅrsak.getText();

            String data = String.format("UPDATE;%d;%s;%s;%s;%s;%s;%s;%s;%s;%b;%s",
                    id, type, kat, besk, kjopsdato, kjopspris, levetid, plassering, antall, tattUt, ars);
            out.println(data);
            System.out.println("Update command sent to server: " + data);
            popupStage.close();
        });

        gridPane.add(labelType, 0, 0);
        gridPane.add(textFieldType, 1, 0);
        gridPane.add(labelKat, 0, 1);
        gridPane.add(textFieldKat, 1, 1);
        gridPane.add(labelBesk, 0, 2);
        gridPane.add(textFieldBesk, 1, 2);
        gridPane.add(labelKjopsdato, 0, 3);
        gridPane.add(textFieldKjopsdato, 1, 3);
        gridPane.add(labelKjopspris, 0, 4);
        gridPane.add(textFieldKjopspris, 1, 4);
        gridPane.add(labelLevetid, 0, 5);
        gridPane.add(textFieldLevetid, 1, 5);
        gridPane.add(labelPlassering, 0, 6);
        gridPane.add(textFieldPlassering, 1, 6);
        gridPane.add(labelAntall, 0, 7);
        gridPane.add(textFieldAntall, 1, 7);
        gridPane.add(labelTattUtAvBruk, 0, 8);
        gridPane.add(checkBoxTattUtAvBruk, 1, 8);
        gridPane.add(labelÅrsak, 0, 9);
        gridPane.add(textFieldÅrsak, 1, 9);
        gridPane.add(updateButton, 1, 10);

        Scene scene = new Scene(gridPane, 400, 350);
        popupStage.setScene(scene);
        popupStage.setTitle("Oppdater vare");
        popupStage.showAndWait();
    }
}
