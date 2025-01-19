package ORM;

import DomainModel.Attrezzatura;
import DomainModel.Postazione;
import DomainModel.Posto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostazioneDAO {
    Connection connection;

    public PostazioneDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Errore nella connessione al DataBase - PostazioneDAO");
            System.out.println(e.getMessage());
        }
    }

    void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<Postazione> getPostazioneDaPrenotazione(int idPrenotazione) {
        List<Postazione> risultato = new ArrayList<>();

        String sql = "SELECT codice, posto, ombrellone FROM prenotazionipostazioni JOIN postazioni on postazione = codice WHERE prenotazione = " +
                idPrenotazione;

        PreparedStatement ps = null;
        ResultSet rs = null;

        //Da AttrezzaturaDAO otteniamo la lista di attrezzatura associata alla Postazione presa in considerazione dopo aver eseguito l'interrogazione
        AttrezzaturaDAO attrezzaturaDAO = new AttrezzaturaDAO();

        try{
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                Posto p = new Posto(rs.getInt("posto"));
                Postazione postazione_tmp = new Postazione(p);
                postazione_tmp.setOmbrellone(rs.getBoolean("ombrellone"));

                //Vengono aggiunte le attrezzature associate all'id di postazione
                postazione_tmp.aggiungiAttrezzatura(attrezzaturaDAO.getAttrezzaturaDaIdPostazione(rs.getInt("codice")));

                //Vengono cancellate le Postazioni estratte: non avendo un id non possono essere sostituite quando verrà modificata una Prenotazione
                //Si lascia al chiamante la responsabilità di preservare i dati
                //TODO una volta implementata la funzione di conferma, decommenta la riga seguente
                //TODO aggiungi, insieme alla chiusura della connessione al momento della chiusura dell'applicazione, il salvataggio della postazione:
//perché, se viene avviata una modifica e il cliente non conferma la prenotazione, i dati precedenti vengono persi
                //Occorre prima rimuovere la postazione dalla relazione postazioneattrezzatura
                //Risolto: prima di aggiungere, ripulire
//                rimuoviPostazione(rs.getInt("codice"));

                risultato.add(new Postazione(postazione_tmp));
            }
        }
        catch (SQLException e) {
            System.out.println("Errore nell'interrogazione' - PostazioneDAO");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return risultato;
    }

    protected void rimuoviPostazione(int idPostazione) {
        String sqlPP = "DELETE FROM prenotazionipostazioni WHERE postazione = "+idPostazione;
        String sqlP = "DELETE FROM postazioni WHERE codice = "+idPostazione;
        String sqlPA = "DELETE FROM postazioneattrezzatura WHERE postazione = "+idPostazione;

        try {
            PreparedStatement ps = connection.prepareStatement(sqlPP);
            ps.executeUpdate();
            ps = connection.prepareStatement(sqlPA);
            ps.executeUpdate();
            ps = connection.prepareStatement(sqlP);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Errore durante la cancellazione - PostazioneDAO");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void inserisciPostazione(Postazione postazione, int codicePrenotazione) {

        //Per prima cosa, viene ottenuto il codice della Postazione

        String sql = "SELECT max(codice)+1 as id FROM postazioni";
        int idPostazione = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                idPostazione = rs.getInt("id");
            }
        }
        catch (SQLException e) {
            System.out.println("Errore nell'accesso al DataBase - PostazioneDAO");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        //Si aggiunge una nuova tupla nella relazione postazioni
        sql = "INSERT INTO postazioni VALUES (" + idPostazione + ", " +
                postazione.getPosto().getNumero() + ", " + postazione.isOmbrellone()
                + ")";


        aggiungiAlDB(sql);

        //Si aggiorna la relazione che associa a ogni Postazione l'attrezzatura selezionata
        AttrezzaturaDAO attrezzaturaDAO = new AttrezzaturaDAO();
        for (Attrezzatura a : postazione.getAttrezzature()) {
            sql = "INSERT INTO postazioneattrezzatura VALUES ( " + idPostazione + ", " +
                    attrezzaturaDAO.getCodiceDaTipo(a.toString()) + //TODO IL METODO NON È ANCORA DEFINITO
                    ", 1) " +
                    "ON CONFLICT (postazione, attrezzatura) DO UPDATE " +
                    "SET quantità = postazioneattrezzatura.quantità + excluded.quantità";
            aggiungiAlDB(sql);
        }

        //Si aggiorna, infine, la relazione che associa le prenotazioni alle postazioni
        sql = "INSERT INTO prenotazionipostazioni VALUES (" + codicePrenotazione + ", " + idPostazione + ")";
        aggiungiAlDB(sql);
    }

    void aggiungiAlDB(String sql) {
        System.out.println(sql);

        PreparedStatement ps = null;

        try{
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Inserimento eseguito con successo");
        }
        catch (SQLException e) {
            System.out.println("Errore nell'inserimento - PostazioneDAO");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void inserisciPostazione(List<Postazione> postazione, int codicePrenotazione) {
        for (Postazione p : postazione) {
            inserisciPostazione(p, codicePrenotazione);
        }
    }

    public List<Integer> getIdPostazioni(int idPrenotazione) {
        List<Integer> idPostazioni = new ArrayList<>();

        String sql = "SELECT postazione FROM prenotazionipostazioni WHERE prenotazione = "+idPrenotazione;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                idPostazioni.add(rs.getInt("postazione"));
            }
        }
        catch(SQLException e) {
            System.out.println("Errore nell'ottenimento del codice delle Postazioni da id Prenotazione - PostazioneDAO");
            System.out.println(e.getMessage());
        }

        return idPostazioni;
    }
}
