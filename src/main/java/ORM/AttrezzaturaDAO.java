package ORM;

import DomainModel.Attrezzatura;
import DomainModel.Lettino;
import DomainModel.Sdraio;
import DomainModel.SediaRegista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttrezzaturaDAO {
    Connection connessione;

    public AttrezzaturaDAO() {
        try {
            connessione = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione - AttrezzaturaDAO");
        }
    }

    public List<Attrezzatura> getAttrezzaturaDaIdPostazione (int idPostazione) {

        List<Attrezzatura> risultato = new ArrayList<>();

        String sql = "SELECT tipo, quantità FROM postazioneattrezzatura join attrezzatura on attrezzatura = codice " +
                "WHERE postazione = " + idPostazione;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connessione.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < rs.getInt("quantità"); i++)
                    risultato.add(getAttrezzaturaDaTipo(rs.getString("tipo")));
            }
        }
        catch (SQLException e) {
            System.out.println("Errore durante l'interrogazione - AttrezzaturaDAO");
            System.out.println(e.getMessage());
        }


        return new ArrayList<>(risultato);
    }

    //TODO FINISCI IL METODO
    private Attrezzatura getAttrezzaturaDaTipo(String tipo) {

        //È usato l'enhanced switch, altrimenti dà errore, perché mancherebbe l'isruzione reutrn
        return switch (tipo) {
            case "Lettino" -> new Lettino();
            case "Sdraio" -> new Sdraio();
            case "Sedia da Regista" -> new SediaRegista();
            default -> null;
        };
    }

    public int getCodiceDaTipo(String tipo) {
        //TODO FINISCI!!!
        return switch (tipo) {
            case "Lettino" -> 1;
            case "Sdraio" -> 2;
            case "Sedia da Regista" -> 3;
            default -> 0;
        };
    }
}
