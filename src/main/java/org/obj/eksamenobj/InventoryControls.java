package org.obj.eksamenobj;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * denne klassen representerer kontrollene for å administrere inventaret,
 * inkludert knapper for å legge til, oppdatere og slette elementer
 */
public class InventoryControls extends HBox {
    private InventoryTable inventoryTable;
    private InventoryService inventoryService;



    /**
     * konstruktør for InventoryControls-klassen
     *
     * @param inventoryTable referanse til inventartabellen
     */

    public InventoryControls(InventoryTable inventoryTable) {
        this.inventoryTable = inventoryTable;
        this.inventoryService = new InventoryService();
        setSpacing(10);
        setPadding(new Insets(10));

        Button addButton = new Button("Legg til inventar");
        Button updateButton = new Button("Oppdater");
        Button deleteButton = new Button("Slett");

        addButton.setOnAction(e -> showInitialPopup());
        updateButton.setOnAction(e -> updateSelectedItem());
        deleteButton.setOnAction(e -> deleteSelectedItem());

        getChildren().addAll(addButton, updateButton, deleteButton);
    }

    /**
     * viser popup-vindu for å velge inventartype ved tillegg av nytt element
     */

    private void showInitialPopup() {
        Stage initialPopupStage = new Stage();
        initialPopupStage.initModality(Modality.APPLICATION_MODAL);
        initialPopupStage.setTitle("Velg Inventartype");

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(10));

        Button moblerButton = new Button("Møbler");
        Button tekniskUtstyrButton = new Button("TekniskUtstyr");
        Button utsmykningButton = new Button("Utsmykning");

        moblerButton.setOnAction(e -> {
            showTypeSpecificPopup("Møbler");
            initialPopupStage.close();
        });

        tekniskUtstyrButton.setOnAction(e -> {
            showTypeSpecificPopup("TekniskUtstyr");
            initialPopupStage.close();
        });

        utsmykningButton.setOnAction(e -> {
            showTypeSpecificPopup("Utsmykning");
            initialPopupStage.close();
        });

        popupVBox.getChildren().addAll(moblerButton, tekniskUtstyrButton, utsmykningButton);
        initialPopupStage.setScene(new Scene(popupVBox, 300, 200));
        initialPopupStage.showAndWait();
    }


    /**
     * viser popup-vindu for å legge til et element av en spesifikk type
     *
     * @param type typen av inventar som skal legges til
     */
    private void showTypeSpecificPopup(String type) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Legg til " + type);

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(10));

        ComboBox<String> kategori = new ComboBox<>();
        if (type.equalsIgnoreCase("Møbler")) {
            kategori.getItems().addAll("bord", "stol", "sofa", "skap", "hylle", "tavle", "annet");
        } else if (type.equalsIgnoreCase("Utsmykning")) {
            kategori.getItems().addAll("maleri", "grafikk", "tekstil", "bilde", "skulptur", "annet");
        }

        TextField beskrivelse = createTextInput("Beskrivelse");
        TextField innkjopsdato = createTextInput("Innkjøpsdato");
        TextField innkjopspris = createTextInput("Innkjøpspris");
        TextField levetid = createTextInput("Forventet levetid");
        TextField plassering = createTextInput("Plassering");
        TextField antall = createTextInput("Antall");
        TextField tattUtAvBruk = createTextInput("Tatt ut av bruk");
        TextField årsak = createTextInput("Årsak");
        Button leggtilKnappPopup = new Button("Legg til vare i inventaret");

        if (type.equalsIgnoreCase("Møbler")) {
            popupVBox.getChildren().addAll(kategori, beskrivelse, innkjopsdato, innkjopspris, levetid, plassering, antall, tattUtAvBruk, årsak, leggtilKnappPopup);
        } else if (type.equalsIgnoreCase("TekniskUtstyr")) {
            TextField kategoriInput = createTextInput("Kategori");
            popupVBox.getChildren().addAll(kategoriInput, beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, leggtilKnappPopup);
        } else if (type.equalsIgnoreCase("Utsmykning")) {
            popupVBox.getChildren().addAll(kategori, beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, leggtilKnappPopup);
        }

        leggtilKnappPopup.setOnAction(e -> {
            if (type.equalsIgnoreCase("TekniskUtstyr")) {
                TextField kategoriInput = (TextField) popupVBox.getChildren().get(0);
                sendCreateRequest(type, kategoriInput.getText(), beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, levetid);
            } else {
                sendCreateRequest(type, kategori.getValue(), beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, levetid);
            }
            popupStage.close();
        });

        popupStage.setScene(new Scene(popupVBox, 300, 500));
        popupStage.showAndWait();
    }

    /**
     * oppdaterer det valgte elementet i inventartabellen
     */

    private void updateSelectedItem() {
        String[] selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            showUpdatePopup(selectedItem);
        }
    }
    /**
     * sletter det valgte elementet i inventartabellen
     */

    private void deleteSelectedItem() {
        String[] selectedItem = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int id = Integer.parseInt(selectedItem[0]);
            inventoryService.deleteInventar(id);
            System.out.println("Delete command executed for ID: " + id);
        }
    }

    /**
     * viser popup-vindu for å oppdatere et valgt element i inventartabellen
     *
     * @param selectedItem det valgte elementet som skal oppdateres
     */

    private void showUpdatePopup(String[] selectedItem) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Oppdater inventar");

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(10));

        TextField inventartype = createTextInput("Inventartype");
        TextField kategori;
        ComboBox<String> kategoriDropdown;

        String type = selectedItem[1];
        if (type.equalsIgnoreCase("Møbler")) {
            kategoriDropdown = new ComboBox<>();
            kategoriDropdown.getItems().addAll("bord", "stol", "sofa", "skap", "hylle", "tavle", "annet");
            kategoriDropdown.setValue(selectedItem[2]);
            kategori = new TextField();
        } else if (type.equalsIgnoreCase("Utsmykning")) {
            kategoriDropdown = new ComboBox<>();
            kategoriDropdown.getItems().addAll("maleri", "grafikk", "tekstil", "bilde", "skulptur", "annet");
            kategoriDropdown.setValue(selectedItem[2]);
            kategori = new TextField();
        } else {
            kategoriDropdown = null;
            kategori = createTextInput("Kategori");
            kategori.setText(selectedItem[2]);
        }

        TextField beskrivelse = createTextInput("Beskrivelse");
        TextField innkjopsdato = createTextInput("Innkjøpsdato");
        TextField innkjopspris = createTextInput("Innkjøpspris");
        TextField levetid = createTextInput("Forventet levetid");
        TextField plassering = createTextInput("Plassering");
        TextField antall = createTextInput("Antall");
        TextField tattUtAvBruk = createTextInput("Tatt ut av bruk");
        TextField årsak = createTextInput("Årsak");
        Button oppdaterKnappPopup = new Button("Oppdater");

        // Pre-fill the fields with the selected item's current values
        inventartype.setText(selectedItem[1]);
        beskrivelse.setText(selectedItem[3]);
        innkjopsdato.setText(selectedItem[4]);
        innkjopspris.setText(selectedItem[5]);
        levetid.setText(selectedItem.length > 6 ? selectedItem[6] : "");
        plassering.setText(selectedItem.length > 7 ? selectedItem[7] : "");
        antall.setText(selectedItem.length > 8 ? selectedItem[8] : "");
        tattUtAvBruk.setText(selectedItem.length > 9 ? selectedItem[9] : "false");
        årsak.setText(selectedItem.length > 10 ? selectedItem[10] : "");

        if (type.equalsIgnoreCase("Møbler") || type.equalsIgnoreCase("Utsmykning")) {
            popupVBox.getChildren().addAll(
                    inventartype, kategoriDropdown, beskrivelse, innkjopsdato, innkjopspris, levetid, plassering, antall, tattUtAvBruk, årsak, oppdaterKnappPopup
            );
        } else {
            popupVBox.getChildren().addAll(
                    inventartype, kategori, beskrivelse, innkjopsdato, innkjopspris, levetid, plassering, antall, tattUtAvBruk, årsak, oppdaterKnappPopup
            );
        }

        Scene popupScene = new Scene(popupVBox, 300, 500);
        popupStage.setScene(popupScene);

        oppdaterKnappPopup.setOnAction(e -> {
            if (type.equalsIgnoreCase("Møbler") || type.equalsIgnoreCase("Utsmykning")) {
                sendUpdateRequest(selectedItem[0], inventartype, kategoriDropdown.getValue(), beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, levetid);
            } else {
                sendUpdateRequest(selectedItem[0], inventartype, kategori.getText(), beskrivelse, innkjopsdato, innkjopspris, plassering, antall, tattUtAvBruk, årsak, levetid);
            }
            popupStage.close();
        });

        popupStage.showAndWait();
    }

    /**
     * sender forespørsel om å opprette et nytt inventarobjekt
     *
     * @param type typen av inventar
     * @param kategori kategorien til inventaret
     * @param beskrivelse beskrivelsen av inventaret
     * @param innkjøpsdato innkjøpsdatoen for inventaret
     * @param innkjøpspris innkjøpsprisen for inventaret
     * @param plassering plasseringen til inventaret
     * @param antall antall enheter av inventaret
     * @param tattUtAvBruk om inventaret er tatt ut av bruk
     * @param årsak årsaken til at inventaret er tatt ut av bruk
     * @param levetid forventet levetid for inventaret
     */

    private void sendCreateRequest(String type, String kategori, TextField beskrivelse, TextField innkjøpsdato, TextField innkjøpspris, TextField plassering, TextField antall, TextField tattUtAvBruk, TextField årsak, TextField levetid) {
        String kat = kategori;
        String besk = beskrivelse.getText();
        String kjøpsdato = innkjøpsdato.getText();
        int kjøpspris = Integer.parseInt(innkjøpspris.getText());
        String plass = plassering.getText();
        int ant = Integer.parseInt(antall.getText());
        boolean utAvBruk = Boolean.parseBoolean(tattUtAvBruk.getText().isEmpty() ? "false" : tattUtAvBruk.getText());
        String arsak = årsak.getText().isEmpty() ? "" : årsak.getText();
        int tid = levetid.getText().isEmpty() ? 0 : Integer.parseInt(levetid.getText());

        Inventar item;
        if (type.equalsIgnoreCase("Møbler")) {
            item = new Møbler(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, tid, utAvBruk, arsak);
        } else if (type.equalsIgnoreCase("TekniskUtstyr")) {
            item = new TekniskUtstyr(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, utAvBruk, arsak);
        } else {
            item = new Utsmykning(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, utAvBruk, arsak);
        }
        inventoryService.addInventar(item);
    }

    /**
     * sender forespørsel om å oppdatere et eksisterende inventarobjekt
     *
     * @param id ID-en til inventarobjektet som skal oppdateres
     * @param inventartype typen av inventar
     * @param kategori kategorien til inventaret
     * @param beskrivelse beskrivelsen av inventaret
     * @param innkjøpsdato innkjøpsdatoen for inventaret
     * @param innkjøpspris innkjøpsprisen for inventaret
     * @param plassering plasseringen til inventaret
     * @param antall antall enheter av inventaret
     * @param tattUtAvBruk om inventaret er tatt ut av bruk
     * @param årsak årsaken til at inventaret er tatt ut av bruk
     * @param levetid forventet levetid for inventaret
     */

    private void sendUpdateRequest(String id, TextField inventartype, String kategori, TextField beskrivelse, TextField innkjøpsdato, TextField innkjøpspris, TextField plassering, TextField antall, TextField tattUtAvBruk, TextField årsak, TextField levetid) {
        Inventar item;
        String type = inventartype.getText();
        String kat = kategori;
        String besk = beskrivelse.getText();
        String kjøpsdato = innkjøpsdato.getText();
        int kjøpspris = Integer.parseInt(innkjøpspris.getText());
        String plass = plassering.getText();
        int ant = Integer.parseInt(antall.getText());
        boolean utAvBruk = Boolean.parseBoolean(tattUtAvBruk.getText().isEmpty() ? "false" : tattUtAvBruk.getText());
        String arsak = årsak.getText().isEmpty() ? "" : årsak.getText();
        int tid = levetid.getText().isEmpty() ? 0 : Integer.parseInt(levetid.getText());

        if (type.equalsIgnoreCase("Møbler")) {
            item = new Møbler(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, tid, utAvBruk, arsak);
        } else if (type.equalsIgnoreCase("TekniskUtstyr")) {
            item = new TekniskUtstyr(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, utAvBruk, arsak);
        } else {
            item = new Utsmykning(type, kat, besk, kjøpsdato, kjøpspris, plass, ant, utAvBruk, arsak);
        }
        item.setId(Integer.parseInt(id));
        inventoryService.updateInventar(item);
    }

    /**
     * oppretter et tekstfelt med gitt tekst for å angi hva feltet er til for
     *
     * @param promptText teksten som skal vises i tekstfeltet
     * @return tekstfeltet som ble opprettet
     */

    private TextField createTextInput(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }
}
