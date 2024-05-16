package org.obj.eksamenobj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class Server {
    private ServerSocket serverSocket;

    public void start(int port) {
        startDatabase();
       try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started and listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
       } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
       }
    }

    private void startDatabase() {
        // Forsøk å etablere databasetilkobling ved oppstart av serveren
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            try {
                conn.close(); // Lukk tilkoblingen siden den bare er for initialisering
            } catch (SQLException e) {
                System.out.println("Feil ved lukking av databasetilkoblingen: " + e.getMessage());
            }
        } else {
            System.out.println("Kunne ikke etablere tilkobling til databasen ved oppstart.");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8000); // Bruk eventuelt en annen port hvis nødvendig
    }
}
