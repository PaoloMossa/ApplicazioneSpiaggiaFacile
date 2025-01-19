package ORM;

import DomainModel.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Errore durante la connessione al database.");
            e.printStackTrace();
        }
    }


    public Cliente getCliente(int idCliente) {
        Cliente c = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM cliente WHERE codice = " + idCliente;
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            System.out.println("Query eseguita");

            if (resultSet.next()) {
                c = new Cliente(resultSet.getString("nome"), resultSet.getString("cognome"), resultSet.getInt("codice"));
                c.setIndiceFedeltà(resultSet.getInt("indice_fedeltà"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Errore durante la query. Cliente non restituito");
        }
        return c;
    }
}
