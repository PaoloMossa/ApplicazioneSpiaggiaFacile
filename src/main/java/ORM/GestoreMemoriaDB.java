package ORM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestoreMemoriaDB {
    private Connection connessione;

    public GestoreMemoriaDB() {
        try {
            connessione = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Connessione fallita - GESTIONE_MEMORIA__DB");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public GestoreMemoriaDB(Connection connessione) {
        this.connessione = connessione;
    }

    public void rimuoviDatiVecchi(int idPrenotazione) {
        String sql = "SELECT postazione FROM prenotazionipostazioni WHERE prenotazione = " + idPrenotazione;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("Interrogazione SQL: " + sql);

        PostazioneDAO postazioneDAO = new PostazioneDAO();

        try {
            ps = connessione.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs!= null && rs.next()) {
                postazioneDAO.rimuoviPostazione(rs.getInt("postazione"));
            }
        }
        catch(SQLException e){
            System.out.println("ERRORE: le vecchie postazioni non sono state rimosse - MEMORY_LEAK");
            System.out.println(e.getMessage());
        }
    }

}
