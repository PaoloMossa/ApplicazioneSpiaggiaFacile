package ORM;

import DomainModel.Postazione;
import DomainModel.Posto;
import DomainModel.SediaRegista;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostazioneDAOTest {

    // Mocks
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);
    private AttrezzaturaDAO mockAttrezzaturaDAO = mock(AttrezzaturaDAO.class);


    @Test
        // Test recupero postazioni da prenotazione esistente
    void testGetPostazioneDaPrenotazioneSuccess() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("codice")).thenReturn(1);
        when(mockResultSet.getInt("posto")).thenReturn(5);
        when(mockResultSet.getBoolean("ombrellone")).thenReturn(true);
        when(mockAttrezzaturaDAO.getAttrezzaturaDaIdPostazione(anyInt())).thenReturn(new ArrayList<>());

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        postazioneDAO.attrezzaturaDAO = mockAttrezzaturaDAO;

        List<Postazione> postazioni = postazioneDAO.getPostazioneDaPrenotazione(1);
        assertEquals(1, postazioni.size());
        assertEquals(5, postazioni.get(0).getPosto().getNumero());
        assertTrue(postazioni.get(0).isOmbrellone());
    }

    @Test
        // Test recupero postazioni da prenotazione inesistente
    void testGetPostazioneDaPrenotazioneEmpty() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        postazioneDAO.attrezzaturaDAO = mockAttrezzaturaDAO;
        List<Postazione> postazioni = postazioneDAO.getPostazioneDaPrenotazione(1);
        assertTrue(postazioni.isEmpty());
    }




    @Test
        // Test rimozione postazione
    void testRimuoviPostazione() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        postazioneDAO.rimuoviPostazione(1);
        verify(mockPreparedStatement, times(3)).executeUpdate();
    }


    @Test
        // Test inserimento singola postazione
    void testInserisciPostazioneSingolaSenzaAttrezzatura() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // First for id retrieval, second for empty result
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockAttrezzaturaDAO.getCodiceDaTipo(anyString())).thenReturn(1);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        postazioneDAO.attrezzaturaDAO = mockAttrezzaturaDAO;


        Postazione postazione = new Postazione(new Posto(1));


        postazioneDAO.inserisciPostazione(postazione, 1);


        verify(mockPreparedStatement, times(2)).executeUpdate(); //2 for insert
    }


    @Test
        //Test inserimento di singola postazione con dell'attrezzatura
    void testInserisciPostazioneSingolaConAttrezzatura() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockAttrezzaturaDAO.getCodiceDaTipo(anyString())).thenReturn(1);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        Postazione postazione = new Postazione(new Posto(1));
        postazione.aggiungiAttrezzatura(new SediaRegista());
        postazioneDAO.inserisciPostazione(postazione, 1);

        verify(mockPreparedStatement, times(3)).executeUpdate(); //Per aggiungere l'attrezzatura, viene eseguito un aggiornamento in più
    }

    @Test
        // Test inserimento lista postazioni
    void testInserisciPostazioneList() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1).thenReturn(2); // Return different IDs for each postazione
        when(mockAttrezzaturaDAO.getCodiceDaTipo(anyString())).thenReturn(1);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        postazioneDAO.attrezzaturaDAO = mockAttrezzaturaDAO;


        List<Postazione> postazioni = new ArrayList<>();
        postazioni.add(new Postazione(new Posto(1)));
        postazioni.add(new Postazione(new Posto(2)));
        postazioneDAO.inserisciPostazione(postazioni, 1);

        verify(mockPreparedStatement, times(4)).executeUpdate();// 2 per ogni Postazione senza attrezzatura
    }

    @Test
        //Test get Id Postazioni from existing reservation
    void testGetIdPostazioniSuccess() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("postazione")).thenReturn(1);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        List<Integer> idPostazioni = postazioneDAO.getIdPostazioni(1);
        assertEquals(1,idPostazioni.size());
        assertEquals(1, idPostazioni.get(0));
    }

    @Test
        //Test get Id Postazioni from non existing reservation
    void testGetIdPostazioniEmpty() throws SQLException{
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        List<Integer> idPostazioni = postazioneDAO.getIdPostazioni(1);
        assertTrue(idPostazioni.isEmpty());
    }



    @Test
    void testAggiungiAlDBSuccess() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        postazioneDAO.aggiungiAlDB("INSERT INTO postazione VALUES (0, 0, 'true')");
        verify(mockPreparedStatement).executeUpdate();
    }




    @Test
    void testGetPostazioneDaPrenotazioneSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        postazioneDAO.attrezzaturaDAO = mockAttrezzaturaDAO;

//        assertThrows(NullPointerException.class,() -> postazioneDAO.getPostazioneDaPrenotazione(1));
//        La riga precedente è stata proposta da Gemini, ma, in realtà, l'eccezione viene gestita all'interno del metodo
        assertDoesNotThrow(() -> postazioneDAO.getPostazioneDaPrenotazione(1));
    }

    @Test
    void testRimuoviPostazioneSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

//        assertThrows(NullPointerException.class, () -> postazioneDAO.rimuoviPostazione(1));
//        La riga precedente è stata proposta da Gemini, ma, in realtà, l'eccezione viene gestita all'interno del metodo
        assertDoesNotThrow(() -> postazioneDAO.getPostazioneDaPrenotazione(1));
    }

    @Test
    void testInserisciPostazioneSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        Postazione postazione = new Postazione(new Posto(1));

//        assertThrows(SQLException.class, () -> postazioneDAO.inserisciPostazione(postazione, 1));
//        La riga precedente è stata proposta da Gemini, ma, in realtà, l'eccezione viene gestita all'interno del metodo
        assertDoesNotThrow(() -> postazioneDAO.inserisciPostazione(postazione, 1));
    }

    @Test
    void testAggiungiAlDBSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        PostazioneDAO postazioneDAO = new PostazioneDAO();
        postazioneDAO.setConnection(mockConnection);

        postazioneDAO.aggiungiAlDB("some sql");
        verify(mockPreparedStatement, never()).executeUpdate();
    }



}