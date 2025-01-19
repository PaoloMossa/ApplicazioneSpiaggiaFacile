package DomainModel;

public class Sdraio implements Attrezzatura{
    double prezzo = PrezziServizi.PREZZO_SDRAIO.getPrezzo();

    @Override
    public double getPrezzo() {
        return prezzo;
    }

    @Override
    public String toString() {
        return "Sdraio";
    }
}
