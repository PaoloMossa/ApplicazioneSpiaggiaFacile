package ORM;

import DomainModel.Richiesta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RichiestaDAO {
    Connection connection;

    public RichiestaDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione: " + e.getMessage());
        }
    }

    public void eseguiRichiesta(int idRichiesta) {
        String sql = "UPDATE richieste " +
                "SET completato = true WHERE codice = " + idRichiesta;

        System.out.println(sql);

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento della richiesta: " + e.getMessage());
        }
    }

    public void nuovaRichiesta(String testo, int idCliente) {
        String sql = "INSERT INTO richieste VALUES ((SELECT max(codice) from richieste) + 1, '" +
                testo + "', " + idCliente + ")";

        System.out.println(sql);

        PreparedStatement ps = null;

        try{
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Richiesta aggiunta: " + testo);
        }
        catch(SQLException e) {
            System.out.println("Errore durante l'inserimento del richiesta: ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Richiesta> ottieniRichieste(int idCliente) {
        List<Richiesta> risultato = new ArrayList<Richiesta>();
        String sql = "SELECT * FROM richieste WHERE completato = false";
        if (idCliente > 0) {
            sql += " AND cliente = " + idCliente;
        }
        System.out.println(sql);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int codice = rs.getInt("codice");
                String testo = rs.getString("testo");
                int cliente = rs.getInt("cliente");
                risultato.add(new Richiesta(codice, testo, cliente));
            }
        } catch (SQLException e) {
            System.out.println("Errore durante l'estrazione delle richieste: " + e.getMessage());
        }


        return new ArrayList<>(risultato);
    }
}
