package ORM;

import DomainModel.Spiaggia;

import java.sql.*;
import java.time.LocalDate;

public class SpiaggiaDAO {
    //TODO impliementa connessione a database
    Connection connection;

    public SpiaggiaDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Errore durante la connessione al database.");
            e.printStackTrace();
        }
    }

    public Spiaggia trovaPostiLiberi(LocalDate inizio) {
        Spiaggia spiaggia = new Spiaggia("Marinella, " + inizio);
//        spiaggia.liberaPosto(3);
//        spiaggia.liberaPosto(58);
//        spiaggia.liberaPosto(13);
//        spiaggia.liberaPosto(30);
//        spiaggia.liberaPosto(23);
//        spiaggia.liberaPosto(5);
        spiaggia = trovaPostiLiberi(inizio, inizio);

        return new Spiaggia(spiaggia);
    }

    public Spiaggia trovaPostiLiberi(LocalDate inizio, LocalDate fine) {
        System.out.println("trovaPostiLiberi(Date inizio, Date fine)");

        Spiaggia spiaggia = new Spiaggia("Marinella, " + inizio + ", " + fine);


        //Trova i numeri dei posti occupati nell'intervallo di tempo dato
        String createView = "CREATE OR REPLACE VIEW postazioni_tra_date(codice) AS " +
                "SELECT pp.postazione " +
                "FROM prenotazionipostazioni pp join prenotazioni p on pp.prenotazione = p.codice " +
                "WHERE data_inizio <= '"+fine+"' AND data_fine >= '"+inizio+"'";

        System.out.println(createView);

        String sql = "SELECT numero FROM posti WHERE numero not in (SELECT posto FROM postazioni p join postazioni_tra_date ptd on p.codice = ptd.codice)";

        System.out.println(sql);

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();

            //SI PUÒ ESEGUIRE SOLO UN'INTERROGAZIONE ALLA VOLTA
            //TODO Cita Chat Gemini 18-12 "Risolvere Problema SQL"
            statement.executeUpdate(createView);

            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                spiaggia.liberaPosto(resultSet.getInt("numero"));
            }
            System.out.println("Query eseguita con successo.");
        }
        catch (SQLException e) {
            System.out.println("Errore durante l'esecuzione dell'interrogazione.");
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }

        return new Spiaggia(spiaggia);
    }
}
