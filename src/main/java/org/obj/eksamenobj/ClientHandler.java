package org.obj.eksamenobj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * håndterer kommunikasjonen med en klient over en socket-forbindelse
 */

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private DatabaseController dbController;


    /**
     * konstruktør som initialiserer ClientHandler med en gitt socket
     *
     * @param socket socket-forbindelsen til klienten
     */
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.dbController = new DatabaseController();
    }

    /**
     * kjører tråden og håndterer innkommende meldinger fra klienten
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                handleInput(inputLine);
                System.out.println("Motatt : " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("Feil: " + this.getId() + ": " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Feil: " + e.getMessage());
            }
        }
    }

    /**
     * håndterer innkommende kommandoer fra klienten
     *
     * @param input kommandoen som mottas fra klienten
     */
    public void handleInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Received an empty command, skipping...");
            return;
        }

        String[] parts = input.split(";");
        System.out.println("Command parts length: " + parts.length);
        for (int i = 0; i < parts.length; i++) {
            System.out.println("Part[" + i + "]: " + parts[i]);
        }

        if (parts.length < 2) {
            System.out.println("Incomplete command, please check the format.");
            return;
        }

        try {
            switch (parts[0]) {
                case "CREATE":
                    createItem(parts);
                    break;
                case "UPDATE":
                    updateItem(parts);
                    break;
                case "DELETE":
                    deleteItem(Integer.parseInt(parts[1]));
                    break;
                case "SEARCH":
                    if (parts.length < 2) {
                        System.out.println("Incomplete command, please check the format.");
                        return;
                    }
                    String[] queries = Arrays.copyOfRange(parts, 1, parts.length);
                    searchItems(queries);
                    break;
                default:
                    System.out.println("Unknown command: " + parts[0]);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Feil : " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * søker etter elementer i databasen basert på mottatte spørringer
     *
     * @param queries søkespørringene
     */

    private void searchItems(String[] queries) {
        List<Inventar> items = dbController.searchItems(queries);
        System.out.println("Search items retrieved: " + items.size() + " items");
        for (Inventar item : items) {
            String itemString;
            if (item instanceof Møbler) {
                itemString = item.getId() + ";" + item.getInventartype() + ";" + item.getKategori() + ";" + item.getBeskrivelse() + ";" + item.getInnkjøpsdato() + ";" + item.getInnkjøpspris() + ";" + ((Møbler) item).getForventetLevetid() + ";" + item.getPlassering() + ";" + item.getAntall() + ";" + item.isTattUtAvBruk() + ";" + item.getÅrsak();
            } else {
                itemString = item.getId() + ";" + item.getInventartype() + ";" + item.getKategori() + ";" + item.getBeskrivelse() + ";" + item.getInnkjøpsdato() + ";" + item.getInnkjøpspris() + ";" + "" + ";" + item.getPlassering() + ";" + item.getAntall() + ";" + item.isTattUtAvBruk() + ";" + item.getÅrsak();
            }
            System.out.println("Sending item: " + itemString);
            out.println(itemString);
        }
        System.out.println("Sending END_OF_SEARCH_RESULTS");
        out.println("END_OF_SEARCH_RESULTS");
    }


    /**
     * oppretter et nytt element i databasen basert på mottatte kommandoer
     *
     * @param parts delene av kommandoen
     * @throws Exception hvis det oppstår en feil ved oppretting av elementet
     */

    private void createItem(String[] parts) throws Exception {
        switch (parts[1]) {
            case "Møbler":
                if (parts.length < 11) {
                    System.out.println("Incomplete command for Møbler, please check the format.");
                    return;
                }
                Møbler møbel = new Møbler(
                        parts[1],                // inventartype
                        parts[2],                // kategori
                        parts[3],                // beskrivelse
                        parts[4],                // innkjøpsdato
                        Integer.parseInt(parts[5]), // innkjøpspris
                        parts[7],                // plassering
                        Integer.parseInt(parts[8]), // antall
                        parts[6].isEmpty() ? 0 : Integer.parseInt(parts[6]), // forventet levetid
                        Boolean.parseBoolean(parts[9]), // tattUtAvBruk
                        parts[10]                // årsak
                );
                dbController.createItem(møbel);
                System.out.println("Element Created: " + møbel.getBeskrivelse());
                break;
            case "TekniskUtstyr":
                if (parts.length < 11) {
                    System.out.println("Incomplete command for TekniskUtstyr, please check the format.");
                    return;
                }
                TekniskUtstyr tekniskUtstyr = new TekniskUtstyr(
                        parts[1],                // inventartype
                        parts[2],                // kategori
                        parts[3],                // beskrivelse
                        parts[4],                // innkjøpsdato
                        Integer.parseInt(parts[5]), // innkjøpspris
                        parts[7],                // plassering
                        Integer.parseInt(parts[8]), // antall
                        Boolean.parseBoolean(parts[9]), // tattUtAvBruk
                        parts[10]                // årsak
                );
                dbController.createItem(tekniskUtstyr);
                System.out.println("Element Created: " + tekniskUtstyr.getBeskrivelse());
                break;
            case "Utsmykning":
                if (parts.length < 11) {
                    System.out.println("Incomplete command for Utsmykning, please check the format.");
                    return;
                }
                Utsmykning utsmykning = new Utsmykning(
                        parts[1],                // inventartype
                        parts[2],                // kategori
                        parts[3],                // beskrivelse
                        parts[4],                // innkjøpsdato
                        Integer.parseInt(parts[5]), // innkjøpspris
                        parts[7],                // plassering
                        Integer.parseInt(parts[8]), // antall
                        Boolean.parseBoolean(parts[9]), // tattUtAvBruk
                        parts[10]                // årsak
                );
                dbController.createItem(utsmykning);
                System.out.println("Element Created: " + utsmykning.getBeskrivelse());
                break;
            default:
                System.out.println("Unsupported item type or incorrect format.");
                break;
        }
    }

    /**
     * oppdaterer et eksisterende element i databasen basert på mottatte kommandoer
     *
     * @param parts delene av kommandoen
     */

    private void updateItem(String[] parts) {
        if (parts.length < 11) {
            System.out.println("Incomplete command, please check the format.");
            return;
        }

        int id = Integer.parseInt(parts[1]);
        String inventartype = parts[2];
        String kategori = parts[3];
        String beskrivelse = parts[4];
        String innkjøpsdato = parts[5];
        int innkjøpspris = Integer.parseInt(parts[6]);
        String forventetLevetidStr = parts[7];
        String plassering = parts[8];
        String antallStr = parts[9];
        boolean tattUtAvBruk = Boolean.parseBoolean(parts[10]);
        String årsak = parts[11];

        int forventetLevetid = forventetLevetidStr.isEmpty() ? 0 : Integer.parseInt(forventetLevetidStr);
        int antall = antallStr.isEmpty() ? 0 : Integer.parseInt(antallStr);

        Inventar item;
        if (inventartype.equalsIgnoreCase("Møbler")) {
            item = new Møbler(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, forventetLevetid, tattUtAvBruk, årsak);
        } else if (inventartype.equalsIgnoreCase("TekniskUtstyr")) {
            item = new TekniskUtstyr(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, tattUtAvBruk, årsak);
        } else {
            item = new Utsmykning(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, tattUtAvBruk, årsak);
        }
        item.setId(id);

        dbController.updateItem(item);
        System.out.println("Item Updated: " + item.getBeskrivelse());
    }

    /**
     * sletter et element fra databasen basert på gitt id
     *
     * @param id id-en til elementet som skal slettes
     */

    private void deleteItem(int id) {
        dbController.deleteItem(id);
        System.out.println("Element Deleted: " + id);
    }
}
