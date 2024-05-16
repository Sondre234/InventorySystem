package org.obj.eksamenobj;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * denne klassen representerer en tabell for å vise inventar fra databasen
 */

public class InventoryTable extends TableView<String[]> {
    private static final String JDBC_URL = "jdbc:sqlite:inventar.db";

    /**
     * konstruktør for InventoryTable-klassen som laster inn tabellen med data fra databasen
     */

    public InventoryTable() {
        loadTable();
    }

    /**
     * laster inn data fra databasen og oppretter kolonner og rader i tabellen
     */

    public void loadTable() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, forventet_levetid, plassering, antall, tatt_ut_av_bruk, årsak FROM Inventar")) {

            getColumns().clear();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i - 1;
                TableColumn<String[], String> column = new TableColumn<>(rs.getMetaData().getColumnName(i));
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[columnIndex]));
                getColumns().add(column);
            }

            while (rs.next()) {
                String[] rowData = new String[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rowData.length; i++) {
                    rowData[i - 1] = rs.getString(i);
                }
                getItems().add(rowData);
            }

            System.out.println("Number of columns in TableView: " + getColumns().size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * oppdaterer tabellen med nye data
     *
     * @param newData en liste av string-arrays som representerer de nye radene som skal legges til tabellen
     */

    public void updateTable(List<String[]> newData) {
        System.out.println("Updating table with new data");
        getItems().clear();

        int columnCount = getColumns().size();
        System.out.println("Column count: " + columnCount);

        for (String[] row : newData) {
            if (row.length < columnCount) {
                String[] extendedRow = new String[columnCount];
                System.arraycopy(row, 0, extendedRow, 0, row.length);
                for (int i = row.length; i < columnCount; i++) {
                    extendedRow[i] = "";
                }
                row = extendedRow;
            } else if (row.length > columnCount) {
                row = Arrays.copyOf(row, columnCount);
            }

            System.out.println("Adding row: " + String.join(", ", row));
            getItems().add(row);
        }
    }

}
