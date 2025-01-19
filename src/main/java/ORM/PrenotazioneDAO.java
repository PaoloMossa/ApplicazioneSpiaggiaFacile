package ORM;

import DomainModel.Cliente;
import DomainModel.Postazione;
import DomainModel.Prenotazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

    Connection connection;
    ClienteDAO clienteDAO = new ClienteDAO();
    PostazioneDAO postazioneDAO = new PostazioneDAO(); //Prima era

    public PrenotazioneDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Errore durante la connessione al database.");
            e.printStackTrace();
        }
    }


    public Prenotazione getPrenotazione(int codice, int idCliente) {
        //Si può evitare l'idCliente, ma l'ho messo perché solo il proprietario della prenotazione può cambiarla, non chiunque abbia il codice di prenotazione

        Prenotazione risultato = null;

        String sql = "SELECT * FROM prenotazioni WHERE codice = " + codice;

        //Si presuppone che il risultato sia uno solo, visto che il codice è chiave primaria
        List<Prenotazione> prenotazioni = getRegistroPrenotazioni(sql, idCliente);

        risultato = prenotazioni.getFirst();


        return new Prenotazione(risultato);
    }


    public Prenotazione nuovaPrenotazione(int idCliente) {
        Prenotazione prenotazione = new Prenotazione();
        int id = getId();
        prenotazione.setId(id);
        //TODO implementa query per ottenere il nome del cliente e gli altri attributi COME METODO!!!
//        Cliente c = new Cliente("prova", "prova", idCliente);
        Cliente c = clienteDAO.getCliente(idCliente);
        System.out.println(c.getNome()+" "+c.getCognome());

        prenotazione.setCliente(new Cliente(c));

        return new Prenotazione(prenotazione);
    }

    private int getId() {
        int id = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT max(codice) FROM prenotazioni"; // Simple query (replace with your actual table and query)
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1) + 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    //TODO completa definizione del metodo getPrenotazioniPassate
    public List<Prenotazione> getPrenotazioniPassate(int idCliente) {
        List<Prenotazione> risultato = new ArrayList<>();

        String sql = "SELECT * FROM prenotazioni WHERE data_fine <= now() ";

        risultato = getRegistroPrenotazioni(sql, idCliente);

        return new ArrayList<>(risultato);
    }

    public List<Prenotazione> getPrenotazioniFuture(int idUtente) {
        List<Prenotazione> risultato = new ArrayList<>();

        String sql = "SELECT * FROM prenotazioni WHERE data_inizio >= now() ";

        risultato = getRegistroPrenotazioni(sql, idUtente);

        return new ArrayList<>(risultato);
    }

    public List<Prenotazione> getPrenotazioniCorrenti(int idCliente) {
        List<Prenotazione> risultato = new ArrayList<>();

        String sql = "SELECT * FROM prenotazioni WHERE data_inizio < now() AND data_fine > now() ";

        risultato = getRegistroPrenotazioni(sql, idCliente);

        return new ArrayList<>(risultato);
    }

    List<Prenotazione> getRegistroPrenotazioni(String sql, int idCliente) {
        //Esegue l'interrogazione e salva in risultato l'insieme delle prenotazioni ottenute

        List<Prenotazione> risultato = new ArrayList<>();

        String selezionePrenotazioni = " AND cliente = " + idCliente;

        //Il cliente 0 è il gestore
        if (idCliente == 0)
            selezionePrenotazioni = "";

        sql += selezionePrenotazioni;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        //parametri di prenotazione
        int pren_id;
        LocalDate pren_dataInizio;
        LocalDate pren_dataFine;
        int pren_cliente;

        try {
            System.out.println("Query: " + sql);

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            System.out.println("Query eseguita");

            //Vengono aggiunte le postazioni trovate
            risultato = aggiungiPostazioniALista(resultSet, new ArrayList<>(risultato)); //FIXME non ricordo perché risultato sia un parametro, sembrerebbe inutile
        }
        catch (SQLException e) {
            System.out.println("Errore durante la query.");
            System.out.println(e.getMessage());
        }

        return new ArrayList<>(risultato);
    }


    List<Prenotazione> aggiungiPostazioniALista(ResultSet resultSet, List<Prenotazione> risultato) throws SQLException {
        int pren_id;
        LocalDate pren_dataFine;
        int pren_cliente;
        LocalDate pren_dataInizio;

        while (resultSet.next()) {
            Prenotazione prenotazione = new Prenotazione();

            pren_id = resultSet.getInt("codice");
            pren_dataInizio = resultSet.getDate("data_inizio").toLocalDate();
            pren_dataFine = resultSet.getDate("data_fine").toLocalDate();
            pren_cliente = resultSet.getInt("cliente");

            prenotazione.setId(pren_id);
            prenotazione.setDate(pren_dataInizio, pren_dataFine);

            //TODO implementa query per ottenere il nome del cliente e gli altri attributi COME METODO!!!
            Cliente c = new Cliente("prova", "prova", pren_cliente);
            prenotazione.setCliente(new Cliente(c));

            //Vengono aggiunte le prenotazioni associate
            //Ora occorre aggiungere le postazioni alla prenotazione
//            PostazioneDAO postazioneDAO = new PostazioneDAO();
            prenotazione.aggiungiPostazione(postazioneDAO.getPostazioneDaPrenotazione(pren_id));

            risultato.add(prenotazione);

            System.out.println(prenotazione.getId());
            System.out.println("totale: "+risultato.size());
        }

        return new ArrayList<>(risultato);
    }

    //TODO completa addPrenotazione
    public void addPrenotazione(Prenotazione prenotazione) {

//        PostazioneDAO postazioneDAO = new PostazioneDAO();

        //Prima, viene aggiunta la prenotazione
        String sql = "INSERT INTO prenotazioni VALUES (" +
                prenotazione.getId() + ", '" + prenotazione.getDataInizio() + "', '" + prenotazione.getDataFine() + "', " +
                prenotazione.getCliente().getIdentificativo() + ") " +
                "ON CONFLICT (codice) DO UPDATE " +
                "SET data_inizio = excluded.data_inizio, " +
                "data_fine = excluded.data_fine";


        PreparedStatement ps = null;
        try {
            System.out.println("Query INSERIMENTO PRENOTAZIONE: " + sql);
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento della PRENOTAZIONE.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //Dato che la Classe Postazione non prevede l'id tra gli attributi, prenotazioneDAO aggiungerà nuove postazioni e, nel caso stiamo
        //modificando una postazione preesistente, è opportuno eliminare le vecchie postazioni salvate nel database
        GestoreMemoriaDB gmDB = new GestoreMemoriaDB(connection);
        gmDB.rimuoviDatiVecchi(prenotazione.getId());


        //Poi, si aggiungono le nuove postazioni e deve essere aggiornata la relazione che associa le prenotazioni alle postazioni
        for (Postazione p : prenotazione.getPostazioni()) {
            postazioneDAO.inserisciPostazione(p, prenotazione.getId());
        }

    }

    public void rimuoviPostazione(int id) {
//        PostazioneDAO postazioneDAO = new PostazioneDAO();
        List<Integer> identificativiPostazione = postazioneDAO.getIdPostazioni(id);
        for (int i : identificativiPostazione) {
            postazioneDAO.rimuoviPostazione(i);
        }

        String sql = "DELETE FROM prenotazioni WHERE codice = " + id;

        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println("Errore durante la rimozione della POSTAZIONE.");
            System.out.println(e.getMessage());
        }
    }
}
