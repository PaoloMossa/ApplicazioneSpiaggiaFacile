package DomainModel;

public class SediaRegista implements Attrezzatura{
    double prezzo = PrezziServizi.PREZZO_REGISTA.getPrezzo();


    @Override
    public double getPrezzo() {
        return prezzo;
    }

    @Override
    public String toString() {
        return "Sedia da Regista";
    }
}
