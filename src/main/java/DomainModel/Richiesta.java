package DomainModel;

public class Richiesta {
    private int id;
    private String testo;
    private int codiceCliente;

    public Richiesta(int id, String testo, int codiceCliente) {
        this.id = id;
        this.testo = testo;
        this.codiceCliente = codiceCliente;
    }

    public int getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }

    public int getCodiceCliente() {
        return codiceCliente;
    }
}
