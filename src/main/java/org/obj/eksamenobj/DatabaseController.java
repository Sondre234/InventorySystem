package org.obj.eksamenobj;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Denne klassen håndterer databaseoperasjoner for inventarobjekter
 */
public class DatabaseController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    /**
     * Oppretter et nytt inventarobjekt i databasen
     *
     * @param inventar Inventar-objektet som skal opprettes
     */
    public void createItem(Inventar inventar) {
        String sql = "INSERT INTO Inventar (inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, tatt_ut_av_bruk, årsak, forventet_levetid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, inventar.getInventartype());
            pstmt.setString(2, inventar.getKategori());
            pstmt.setString(3, inventar.getBeskrivelse());
            pstmt.setString(4, inventar.getInnkjøpsdato());
            pstmt.setInt(5, inventar.getInnkjøpspris());
            pstmt.setString(6, inventar.getPlassering());
            pstmt.setInt(7, inventar.getAntall());
            pstmt.setBoolean(8, inventar.isTattUtAvBruk());
            pstmt.setString(9, inventar.getÅrsak());
            if (inventar instanceof Møbler) {
                pstmt.setInt(10, ((Møbler) inventar).getForventetLevetid());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    inventar.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("SQL Feil: ", e);
        }
    }

    /**
     * Oppdaterer et eksisterende inventarobjekt i databasen
     *
     * @param inventar Inventar-objektet som skal oppdateres
     */
    public void updateItem(Inventar inventar) {
        String sql = "UPDATE Inventar SET inventartype = ?, kategori = ?, beskrivelse = ?, innkjøpsdato = ?, innkjøpspris = ?, plassering = ?, antall = ?, tatt_ut_av_bruk = ?, årsak = ?, forventet_levetid = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inventar.getInventartype());
            pstmt.setString(2, inventar.getKategori());
            pstmt.setString(3, inventar.getBeskrivelse());
            pstmt.setString(4, inventar.getInnkjøpsdato());
            pstmt.setInt(5, inventar.getInnkjøpspris());
            pstmt.setString(6, inventar.getPlassering());
            pstmt.setInt(7, inventar.getAntall());
            pstmt.setBoolean(8, inventar.isTattUtAvBruk());
            pstmt.setString(9, inventar.getÅrsak());
            if (inventar instanceof Møbler) {
                pstmt.setInt(10, ((Møbler) inventar).getForventetLevetid());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }
            pstmt.setInt(11, inventar.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Item updated successfully");
            }
        } catch (SQLException e) {
            logger.error("SQL Feil ", e);
        }
    }

    /**
     * Sletter et inventarobjekt fra databasen
     *
     * @param id ID-en til inventarobjektet som skal slettes
     */
    public void deleteItem(int id) {
        String sql = "DELETE FROM Inventar WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            logger.info("Deleted {} row(s) with id: {}", rowsAffected, id);
        } catch (SQLException e) {
            logger.error("SQL Feil: ", e);
        }
    }

    /**
     * Søker etter inventar i databasen basert på typedefinerte søkemåter.
     * Søkestrengene for intervall må være i et spesifikt format for å matche disse feltene:
     * - "innkjøpspris:x-y" for å søke på innkjøpspris mellom x og y
     * - "antall:x-y" for å søke på antall mellom x og y
     * - "levetidsforventning:x-y" for å søke på forventet levetid mellom x og y
     * - annen tekst vil søke på kategori, beskrivelse, innkjøpsdato, plassering eller årsak med en "LIKE" SQL-query.
     *
     * @param søkeQueries en array av string som representerer de forskjellige søkemåtene. Hver query kan være fri tekst eller et intervall på spesifikke felt.
     * @return en liste med inventarobjekter som matcher de gitte søkekriteriene. Listen kan være tom hvis ingen objekter matcher søket.
     */
    public List<Inventar> searchItems(String[] søkeQueries) {
        List<Inventar> inventar = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, forventet_levetid, plassering, antall, tatt_ut_av_bruk, årsak FROM Inventar WHERE ");
        List<String> conditions = new ArrayList<>();
        List<Object> intervall = new ArrayList<>();
        List<Integer> søkeType = new ArrayList<>();

        for (String query : søkeQueries) {
            if (query.matches("innkjøpspris:\\d+-\\d+")) {
                conditions.add("(innkjøpspris BETWEEN ? AND ?)");
                String[] range = query.split(":")[1].split("-");
                intervall.add(Integer.parseInt(range[0]));
                intervall.add(Integer.parseInt(range[1]));
                søkeType.add(1);
            } else if (query.matches("antall:\\d+-\\d+")) {
                conditions.add("(antall BETWEEN ? AND ?)");
                String[] range = query.split(":")[1].split("-");
                intervall.add(Integer.parseInt(range[0]));
                intervall.add(Integer.parseInt(range[1]));
                søkeType.add(1);
            } else if (query.matches("forventet_levetid:\\d+-\\d+")) {
                conditions.add("(forventet_levetid BETWEEN ? AND ?)");
                String[] range = query.split(":")[1].split("-");
                intervall.add(Integer.parseInt(range[0]));
                intervall.add(Integer.parseInt(range[1]));
                søkeType.add(1);
            } else if (query.matches("ubrukelig:(true|false)")) {
                conditions.add("tatt_ut_av_bruk = ?");
                intervall.add(Boolean.parseBoolean(query.split(":")[1]));
                søkeType.add(2);
            } else {
                conditions.add("(inventartype LIKE ? OR kategori LIKE ? OR beskrivelse LIKE ? OR innkjøpsdato LIKE ? OR plassering LIKE ? OR årsak LIKE ?)");
                for (int i = 0; i < 6; i++) {
                    intervall.add("%" + query + "%");
                }
                søkeType.add(0);
            }
        }

        if (conditions.isEmpty()) {
            System.out.println("No search conditions provided.");
            return inventar;
        }

        sql.append(String.join(" AND ", conditions));

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            for (int i = 0; i < søkeType.size(); i++) {
                if (søkeType.get(i) == 1) {
                    pstmt.setInt(index++, (Integer) intervall.remove(0));
                    pstmt.setInt(index++, (Integer) intervall.remove(0));
                } else if (søkeType.get(i) == 2) {
                    pstmt.setBoolean(index++, (Boolean) intervall.remove(0));
                } else {
                    for (int j = 0; j < 6; j++) {
                        pstmt.setString(index++, (String) intervall.remove(0));
                    }
                }
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String inventartype = rs.getString("inventartype");
                String kategori = rs.getString("kategori");
                String beskrivelse = rs.getString("beskrivelse");
                String innkjøpsdato = rs.getString("innkjøpsdato");
                int innkjøpspris = rs.getInt("innkjøpspris");
                String forventetLevetid = rs.getString("forventet_levetid");
                String plassering = rs.getString("plassering");
                int antall = rs.getInt("antall");
                boolean tattUtAvBruk = rs.getBoolean("tatt_ut_av_bruk");
                String årsak = rs.getString("årsak");

                if (forventetLevetid == null) {
                    forventetLevetid = ""; // Default value for null
                }

                Inventar objekt;
                if ("Møbler".equals(inventartype)) {
                    objekt = new Møbler(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, Integer.parseInt(forventetLevetid), tattUtAvBruk, årsak);
                } else if ("TekniskUtstyr".equals(inventartype)) {
                    objekt = new TekniskUtstyr(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, tattUtAvBruk, årsak);
                } else {
                    objekt = new Utsmykning(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall, tattUtAvBruk, årsak);
                }
                objekt.setId(id);
                inventar.add(objekt);
            }
        } catch (SQLException e) {
            logger.error("Feil under søk: ", e);
        }
        return inventar;
    }
}
