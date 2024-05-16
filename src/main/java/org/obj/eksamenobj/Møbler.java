package org.obj.eksamenobj;

public class Møbler extends Inventar {
    private int forventetLevetid;

    public Møbler(String inventartype, String kategori, String beskrivelse, String innkjøpsdato, int innkjøpspris, String plassering, int antall, int forventetLevetid, boolean tattUtAvBruk, String årsak) {
        super(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall);
        this.forventetLevetid = forventetLevetid;
        this.tattUtAvBruk = tattUtAvBruk;
        this.årsak = årsak;
    }

    // Getter for forventet levetid
    public int getForventetLevetid() {
        return forventetLevetid;
    }

}
