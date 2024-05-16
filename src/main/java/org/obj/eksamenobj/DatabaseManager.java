package org.obj.eksamenobj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * denne klassen håndterer oppkobling og oppsett av databasen
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:inventar.db";

    /**
     * kobler til SQLite-databasen
     *
     * @return Connection objektet som representerer tilkoblingen til databasen
     */

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Koblingen til databasen ble opprettet.");
        } catch (SQLException e) {
            System.out.println("Feil: " + e.getMessage());
        }
        return conn;
    }

    /**
     * setter opp databasen ved å opprette nødvendig tabell hvis den ikke allerede eksisterer
     */

    public static void setupDatabase() {
        String inventar = "CREATE TABLE IF NOT EXISTS Inventar (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "inventartype VARCHAR(255) NOT NULL, " +
                "kategori VARCHAR(255) NOT NULL, " +
                "beskrivelse VARCHAR(255), " +
                "innkjøpsdato INTEGER NOT NULL, " +
                "innkjøpspris INTEGER NOT NULL, " +
                "forventet_levetid INTEGER, " +
                "plassering VARCHAR(255), " +
                "antall INTEGER, " +
                "tatt_ut_av_bruk BOOLEAN, " +
                "årsak VARCHAR(255)" +
                ");";


        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.addBatch(inventar);
            stmt.executeBatch();
            System.out.println("Tabellen ble opprettet eller eksisterer allerede.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * main-metode for å sette opp databasen
     * @param args
     */

    public static void main(String[] args) {
        setupDatabase();
    }
}
