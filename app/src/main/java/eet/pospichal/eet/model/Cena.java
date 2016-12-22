package eet.pospichal.eet.model;

/**
 * Created by pospile on 17/12/2016.
 */

public class Cena {
    public int koruny;
    public int halere;
    public Dan.Typ dan;
    public int dan_koruny;
    public int dan_halere;

    public Cena (int koruny, int halere, Dan.Typ dan){
        this.koruny = koruny;
        this.halere = halere;
        this.dan = dan;
    }
}
