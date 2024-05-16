package org.obj.eksamenobj;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerCommunication {
    private PrintWriter out;
    private BufferedReader in;
    private InventoryTable inventoryTable;

    public ServerCommunication(InventoryView inventoryView) {
        this.inventoryTable = inventoryView.getInventoryTable();
        try {
            Socket socket = new Socket("localhost", 8000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public void startListening() {
        new Thread(() -> {
            try {
                String response;
                List<String[]> results = new ArrayList<>();
                while ((response = in.readLine()) != null) {
                    if (response.equals("END_OF_SEARCH_RESULTS")) {
                        List<String[]> finalResults = new ArrayList<>(results);
                        Platform.runLater(() -> inventoryTable.updateTable(finalResults));
                        results.clear();
                    } else {
                        results.add(response.split(";"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() throws IOException {
        if (out != null) out.close();
        if (in != null) in.close();
    }
}
