package org.obj.eksamenobj;

public class Utsmykning extends Inventar {
    public Utsmykning(String inventartype, String kategori, String beskrivelse, String innkjøpsdato, int innkjøpspris, String plassering, int antall, boolean tattUtAvBruk, String årsak) {
        super(inventartype, kategori, beskrivelse, innkjøpsdato, innkjøpspris, plassering, antall);
        this.tattUtAvBruk = tattUtAvBruk;
        this.årsak = årsak;
    }

    // Utsmykning-specific methods can be added here
}
