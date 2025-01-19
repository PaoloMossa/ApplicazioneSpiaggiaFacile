package DomainModel;

public enum PrezziServizi {
    PREZZO_OMBRELLONE(15.0), PREZZO_LETTINO(10.0), PREZZO_REGISTA(5.0), PREZZO_SDRAIO(5.0);

    private double prezzo;
    PrezziServizi(double prezzo) {
        this.prezzo = prezzo;
    }
    public double getPrezzo() {
        return prezzo;
    }
}
