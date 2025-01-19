package DomainModel;

public class Lettino implements Attrezzatura{
    double prezzo = PrezziServizi.PREZZO_LETTINO.getPrezzo();


    @Override
    public double getPrezzo () {
        return prezzo;
    }

    @Override
    public String toString() {
        return "Lettino";
    }
}