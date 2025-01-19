package ORM;

import DomainModel.Cliente;
import DomainModel.Postazione;
import DomainModel.Posto;
import DomainModel.Prenotazione;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PrenotazioneDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private ClienteDAO mockClienteDAO;
    @Mock
    private PostazioneDAO mockPostazioneDAO;


    private PrenotazioneDAO prenotazioneDAO;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        prenotazioneDAO = new PrenotazioneDAO();
        prenotazioneDAO.connection = mockConnection;
        prenotazioneDAO.clienteDAO = mockClienteDAO;
        prenotazioneDAO.postazioneDAO = mockPostazioneDAO;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetPrenotazione() throws SQLException {
        // Arrange
        int codice = 1;
        int idCliente = 1;
        Prenotazione expectedPrenotazione = new Prenotazione();
        expectedPrenotazione.setId(codice);
        expectedPrenotazione.setDate(LocalDate.now(), LocalDate.now().plusDays(1));
        Cliente cliente = new Cliente("nome", "cognome", idCliente);
        expectedPrenotazione.setCliente(cliente);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("codice")).thenReturn(codice);
        when(mockResultSet.getDate("data_inizio")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("data_fine")).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
        when(mockResultSet.getInt("cliente")).thenReturn(idCliente);
        when(mockClienteDAO.getCliente(idCliente)).thenReturn(cliente);

        // Mock the PostazioneDAO
        PrenotazioneDAO spyPrenotazioneDAO = spy(prenotazioneDAO);
        List<Prenotazione> list = new ArrayList<>();
        list.add(expectedPrenotazione);
        doReturn(list).when(spyPrenotazioneDAO).getRegistroPrenotazioni(anyString(), anyInt());


        // Act
        Prenotazione actualPrenotazione = spyPrenotazioneDAO.getPrenotazione(codice, idCliente);

        // Assert
        assertEquals(expectedPrenotazione.getId(), actualPrenotazione.getId());
        assertEquals(expectedPrenotazione.getDataInizio(), actualPrenotazione.getDataInizio());
        assertEquals(expectedPrenotazione.getDataFine(), actualPrenotazione.getDataFine());
        assertEquals(expectedPrenotazione.getCliente().getIdentificativo(), actualPrenotazione.getCliente().getIdentificativo());
    }

    @Test
    void testNuovaPrenotazione() throws SQLException {
        // Arrange
        int idCliente = 1;
        Cliente cliente = new Cliente("nome", "cognome", idCliente);
        when(mockClienteDAO.getCliente(idCliente)).thenReturn(cliente);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt(1)).thenReturn(100); // Assuming the next ID will be 101

        // Act
        Prenotazione actualPrenotazione = prenotazioneDAO.nuovaPrenotazione(idCliente);

        // Assert
        assertEquals(101, actualPrenotazione.getId());
        assertEquals(cliente.getNome(), actualPrenotazione.getCliente().getNome());
        assertEquals(cliente.getCognome(), actualPrenotazione.getCliente().getCognome());
        assertEquals(idCliente, actualPrenotazione.getCliente().getIdentificativo());
        verify(mockClienteDAO).getCliente(idCliente);
        verify(mockConnection).prepareStatement(eq("SELECT max(codice) FROM prenotazioni"));
        verify(mockPreparedStatement).executeQuery();
    }


    @Test
    void testGetPrenotazioniPassate() throws SQLException {
        // Arrange
        int idCliente = 1;
        List<Prenotazione> expectedPrenotazioni = new ArrayList<>();
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        prenotazione.setDate(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        prenotazione.setCliente(new Cliente("nome", "cognome", idCliente));
        expectedPrenotazioni.add(prenotazione);

        PrenotazioneDAO spyPrenotazioneDAO = spy(prenotazioneDAO);
        doReturn(expectedPrenotazioni).when(spyPrenotazioneDAO).getRegistroPrenotazioni(anyString(), anyInt());

        // Act
        List<Prenotazione> actualPrenotazioni = spyPrenotazioneDAO.getPrenotazioniPassate(idCliente);

        // Assert
        assertEquals(expectedPrenotazioni.size(), actualPrenotazioni.size());
        assertEquals(expectedPrenotazioni.get(0).getId(), actualPrenotazioni.get(0).getId());
        verify(spyPrenotazioneDAO).getRegistroPrenotazioni(startsWith("SELECT * FROM prenotazioni WHERE data_fine <= now() "), eq(idCliente));
    }

    @Test
    void testGetPrenotazioniFuture() throws SQLException {
        // Arrange
        int idCliente = 1;
        List<Prenotazione> expectedPrenotazioni = new ArrayList<>();
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        prenotazione.setDate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        prenotazione.setCliente(new Cliente("nome", "cognome", idCliente));
        expectedPrenotazioni.add(prenotazione);

        PrenotazioneDAO spyPrenotazioneDAO = spy(prenotazioneDAO);
        doReturn(expectedPrenotazioni).when(spyPrenotazioneDAO).getRegistroPrenotazioni(anyString(), anyInt());

        // Act
        List<Prenotazione> actualPrenotazioni = spyPrenotazioneDAO.getPrenotazioniFuture(idCliente);

        // Assert
        assertEquals(expectedPrenotazioni.size(), actualPrenotazioni.size());
        assertEquals(expectedPrenotazioni.get(0).getId(), actualPrenotazioni.get(0).getId());
        verify(spyPrenotazioneDAO).getRegistroPrenotazioni(startsWith("SELECT * FROM prenotazioni WHERE data_inizio >= now() "), eq(idCliente));
    }

    @Test
    void testGetPrenotazioniCorrenti() throws SQLException {
        // Arrange
        int idCliente = 1;
        List<Prenotazione> expectedPrenotazioni = new ArrayList<>();
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        prenotazione.setDate(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        prenotazione.setCliente(new Cliente("nome", "cognome", idCliente));
        expectedPrenotazioni.add(prenotazione);

        PrenotazioneDAO spyPrenotazioneDAO = spy(prenotazioneDAO);
        doReturn(expectedPrenotazioni).when(spyPrenotazioneDAO).getRegistroPrenotazioni(anyString(), anyInt());

        // Act
        List<Prenotazione> actualPrenotazioni = spyPrenotazioneDAO.getPrenotazioniCorrenti(idCliente);

        // Assert
        assertEquals(expectedPrenotazioni.size(), actualPrenotazioni.size());
        assertEquals(expectedPrenotazioni.get(0).getId(), actualPrenotazioni.get(0).getId());
        verify(spyPrenotazioneDAO).getRegistroPrenotazioni(startsWith("SELECT * FROM prenotazioni WHERE data_inizio < now() AND data_fine > now() "), eq(idCliente));
    }

    @Test
    void testGetRegistroPrenotazioni() throws SQLException {
        // Arrange
        int idCliente = 1;
        String sql = "SELECT * FROM prenotazioni WHERE codice = 1";
        List<Prenotazione> expectedPrenotazioni = new ArrayList<>();
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        prenotazione.setDate(LocalDate.now(), LocalDate.now().plusDays(1));
        prenotazione.setCliente(new Cliente("nome", "cognome", idCliente));
        expectedPrenotazioni.add(prenotazione);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("codice")).thenReturn(1);
        when(mockResultSet.getDate("data_inizio")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("data_fine")).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
        when(mockResultSet.getInt("cliente")).thenReturn(idCliente);

        PrenotazioneDAO spyPrenotazioneDAO = spy(prenotazioneDAO);
        doReturn(expectedPrenotazioni).when(spyPrenotazioneDAO).aggiungiPostazioniALista(any(), anyList());

        // Act
        List<Prenotazione> actualPrenotazioni = spyPrenotazioneDAO.getRegistroPrenotazioni(sql, idCliente);

        // Assert
        assertEquals(expectedPrenotazioni.size(), actualPrenotazioni.size());
        assertEquals(expectedPrenotazioni.get(0).getId(), actualPrenotazioni.get(0).getId());
        verify(mockConnection).prepareStatement(eq(sql+" AND cliente = 1"));
        verify(mockPreparedStatement).executeQuery();
        verify(spyPrenotazioneDAO).aggiungiPostazioniALista(eq(mockResultSet), anyList());

    }

    @Test
    void testAggiungiPostazioniALista() throws SQLException {
        // Arrange
        List<Prenotazione> prenotazioni = new ArrayList<>();
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("codice")).thenReturn(1);
        when(mockResultSet.getDate("data_inizio")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("data_fine")).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
        when(mockResultSet.getInt("cliente")).thenReturn(1);

        Postazione postazione = new Postazione(new Posto(1));
        List<Postazione> postazioni = new ArrayList<>();
        postazioni.add(postazione);
        when(mockPostazioneDAO.getPostazioneDaPrenotazione(1)).thenReturn(postazioni);


        // Act
        List<Prenotazione> result = prenotazioneDAO.aggiungiPostazioniALista(mockResultSet, prenotazioni);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(LocalDate.now(), result.get(0).getDataInizio());
        assertEquals(LocalDate.now().plusDays(1), result.get(0).getDataFine());
        assertEquals(1, result.get(0).getCliente().getIdentificativo());
        verify(mockResultSet, times(2)).next();
        verify(mockPostazioneDAO).getPostazioneDaPrenotazione(1);
    }

    @Test
    void testAddPrenotazione() throws SQLException {
        // Arrange
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setId(1);
        prenotazione.setDate(LocalDate.now(), LocalDate.now().plusDays(1));
        Cliente cliente = new Cliente("nome", "cognome", 1);
        prenotazione.setCliente(cliente);
        Postazione postazione = new Postazione(new Posto(1));
        prenotazione.aggiungiPostazione(postazione);

        PostazioneDAO mockPostazioneDAO = mock(PostazioneDAO.class);

        prenotazioneDAO.postazioneDAO = mockPostazioneDAO;

        prenotazioneDAO.addPrenotazione(prenotazione);

        verify(mockConnection, atLeastOnce()).prepareStatement(anyString()); // Verify that prepareStatement was called at least once
        verify(mockPreparedStatement, atLeastOnce()).executeUpdate(); // Verify that executeUpdate was called at least once
        verify(mockPostazioneDAO).inserisciPostazione(eq(postazione), eq(prenotazione.getId()));
    }

    @Test
    void testRimuoviPostazione() throws SQLException {
        // Arrange
        int id = 1;
        List<Integer> idsPostazione = new ArrayList<>();
        idsPostazione.add(2);
        idsPostazione.add(3);
        when(mockPostazioneDAO.getIdPostazioni(id)).thenReturn(idsPostazione);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Act
        prenotazioneDAO.rimuoviPostazione(id);

        // Assert
        verify(mockPostazioneDAO).getIdPostazioni(id);
        verify(mockPostazioneDAO).rimuoviPostazione(2);
        verify(mockPostazioneDAO).rimuoviPostazione(3);
        verify(mockConnection).prepareStatement(eq("DELETE FROM prenotazioni WHERE codice = " + id));
        verify(mockPreparedStatement).executeUpdate();
    }
}