package org.obj.eksamenobj;

/**
 * denne klassen håndterer operasjoner relatert til inventar, inkludert oppretting, oppdatering og sletting
 */


public class InventoryService {
    private DatabaseController dbController;

    /**
     * konstruktør for InventoryService-klassen som initialiserer DatabaseController
     */

    public InventoryService() {
        this.dbController = new DatabaseController();
    }


    /**
     * legger til et nytt inventarobjekt i databasen
     *
     * @param item inventarobjektet som skal legges til
     */
    public void addInventar(Inventar item) {
        dbController.createItem(item);
    }

    /**
     * oppdaterer et eksisterende inventarobjekt i databasen
     *
     * @param item inventarobjektet som skal oppdateres
     */

    public void updateInventar(Inventar item) {
        dbController.updateItem(item);
    }

    /**
     * sletter et inventarobjekt fra databasen basert på ID
     *
     * @param id ID-en til inventarobjektet som skal slettes
     */

    public void deleteInventar(int id) {
        dbController.deleteItem(id);
    }
}
