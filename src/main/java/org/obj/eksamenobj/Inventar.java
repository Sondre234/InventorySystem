package org.obj.eksamenobj;

/**
 * abstrakt klasse som representerer et inventarobjekt
 */

public abstract class Inventar {
    protected int id;
    protected String inventartype; // New field
    protected String kategori;
    protected String beskrivelse;
    protected String innkjøpsdato;
    protected int innkjøpspris;
    protected String plassering;
    protected int antall;
    protected boolean tattUtAvBruk;
    protected String årsak;

    /**
     * konstruktør for Inventar-klassen
     *
     * @param inventartype typen av inventar
     * @param kategori kategorien til inventaret
     * @param beskrivelse beskrivelse av inventaret
     * @param innkjøpsdato innkjøpsdato for inventaret
     * @param innkjøpspris innkjøpspris for inventaret
     * @param plassering plassering av inventaret
     * @param antall antall enheter av inventaret
     */

    public Inventar(String inventartype, String kategori, String beskrivelse, String innkjøpsdato, int innkjøpspris, String plassering, int antall) {
        this.inventartype = inventartype;
        this.kategori = kategori;
        this.beskrivelse = beskrivelse;
        this.innkjøpsdato = innkjøpsdato;
        this.innkjøpspris = innkjøpspris;
        this.plassering = plassering;
        this.antall = antall;
    }

    // Gettere og settere for nye felt
    /**
     *
     */
    public String getInventartype() {
        return inventartype;
    }
    public void setInventartype(String inventartype) {
        this.inventartype = inventartype;
    }

    // Getters and setters for new fields
    public boolean isTattUtAvBruk() {
        return tattUtAvBruk;
    }

    public String getÅrsak() {
        return årsak;
    }
    public void setÅrsak(String årsak) {
        this.årsak = årsak;
    }

    // Other getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }
    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getInnkjøpsdato() {
        return innkjøpsdato;
    }
    public void setInnkjøpsdato(String innkjøpsdato) {
        this.innkjøpsdato = innkjøpsdato;
    }

    public int getInnkjøpspris() {
        return innkjøpspris;
    }
    public void setInnkjøpspris(int innkjøpspris) {
        this.innkjøpspris = innkjøpspris;
    }

    public String getPlassering() {
        return plassering;
    }
    public void setPlassering(String plassering) {
        this.plassering = plassering;
    }

    public int getAntall() {
        return antall;
    }
    public void setAntall(int antall) {
        this.antall = antall;
    }
}
