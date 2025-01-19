package DomainModel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Prenotazione {
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private List<Postazione> postazioni;
    private Cliente cliente;
    private int id;

    public Prenotazione () {
        postazioni = new ArrayList<>();
        cliente = new Cliente();
    }

    public Prenotazione (Prenotazione prenotazione) {
        this.dataInizio = prenotazione.dataInizio;
        this.dataFine = prenotazione.dataFine;
        this.postazioni = prenotazione.postazioni;
        this.cliente = prenotazione.cliente;
        this.id = prenotazione.id;
    }


    public double getPrezzo() {
        double prezzo = 0;
        for (Postazione postazione : postazioni)
            prezzo += postazione.getPrezzo();
        double giorni = (ChronoUnit.DAYS.between(dataInizio, dataFine)) + 1; //Il primo giorno non viene contato
        return prezzo*(giorni);
    }

    public void setId (int id) {
        this.id = id;
    }
    public int getId () {
        return id;
    }

    public void setDate (LocalDate dataInizio, LocalDate dataFine) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public LocalDate getDataInizio () {
        return dataInizio;
    }
    public LocalDate getDataFine () {
        return dataFine;
    }

    public void aggiungiPostazione (Postazione postazione) {
        postazioni.add(postazione);
    }

    public void aggiungiPostazione (List<Postazione> postazioni) {
        this.postazioni.addAll(postazioni);
    }

    public void setCliente (Cliente cliente) {
        this.cliente = cliente;
    }
    public Cliente getCliente () {
        return cliente;
    }

    public List<Postazione> getPostazioni () {
        return new ArrayList<>(postazioni);
    }

    public void clearPostazioni() {
        postazioni.clear();
    }
}
