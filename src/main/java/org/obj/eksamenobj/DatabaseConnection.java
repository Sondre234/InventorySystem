package org.obj.eksamenobj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The {@code DatabaseConnection} Klasse som oppretter databasetilkobling
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:inventar.db";  //knytter til databasepathen

    /**
     * oppretter databasetilkobling
     *
     * @return  {@link Connection} objekt, eller null hvis tilkoblingen feiler
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Databasetilkobling suksess!");
        } catch (SQLException e) {
            System.out.println("Databasetilkobling feilet: " + e.getMessage());
        }
        return conn;
    }
}
