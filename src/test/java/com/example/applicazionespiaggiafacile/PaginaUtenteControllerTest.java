package com.example.applicazionespiaggiafacile;

import DomainModel.*;
import ORM.PostoDAO;
import ORM.PrenotazioneDAO;
import ORM.SpiaggiaDAO;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaginaUtenteControllerTest extends ApplicationTest {

    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);

    @Mock
    private SpiaggiaDAO mockSpiaggiaDAO = mock(SpiaggiaDAO.class);

    @Mock
    private PrenotazioneDAO prenotazioneDAO = mock(PrenotazioneDAO.class);

    @Mock
    private PostoDAO postoDAO = mock(PostoDAO.class);

    @InjectMocks
    private PaginaUtenteController controller;

    private Stage stage;

    @BeforeAll
    public static void setUpClass() throws InterruptedException {
        // Initialize JavaFX toolkit
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await(5, TimeUnit.SECONDS);
    }


    @Override
    @Start
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("paginaUtente.fxml"));

                Parent root = loader.load();

                controller = loader.getController();
                controller.setSpiaggiaDAO(mockSpiaggiaDAO);
                controller.setPrenotazioneDAO(prenotazioneDAO);
                controller.setPostoDAO(postoDAO);


                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        // Initialize mocks and controller
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MockitoAnnotations.openMocks(this);
            controller.setIdUtente(1);
            controller.setPrenotazione(new Prenotazione());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);

    }

    @Test
    public void testImpostaDataInizio() throws InterruptedException {
        // Mock data

        List<Integer> postiLiberi = new ArrayList<>();
        postiLiberi.add(1);
        postiLiberi.add(2);
        Spiaggia spiaggia = new Spiaggia("");
        spiaggia.setPostiLiberi(postiLiberi);

        // Mock behavior
        when(mockSpiaggiaDAO.trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class))).thenReturn(spiaggia);
        when(prenotazioneDAO.nuovaPrenotazione(anyInt())).thenReturn(new Prenotazione());

        // Execute test on JavaFX thread
        CountDownLatch latch = new CountDownLatch(1);

        // Set data inizio
        clickOn("#dataInizio").write(tomorrow.toString()).type(KeyCode.ENTER);
        clickOn("#dataFine").type(KeyCode.END).eraseText(10);
        clickOn("#dataFine").write(tomorrow.plusDays(1).toString()).type(KeyCode.ENTER);
        clickOn("#confermaDate");

        // Verify that the available seats are updated correctly
        assertEquals(2, controller.getPosti().size());
        assertTrue(controller.getPosti().contains(1));
        assertTrue(controller.getPosti().contains(2));
        latch.countDown();

        latch.await(5, TimeUnit.SECONDS);

        // Verify interactions
        verify(mockSpiaggiaDAO, times(1)).trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class));
    }
    @Test
    public void testAggiungiPosto() throws InterruptedException {
        // Mock data
        List<Integer> postiLiberi = new ArrayList<>();
        postiLiberi.add(1);
        postiLiberi.add(2);
        Spiaggia spiaggia = new Spiaggia("");
        spiaggia.setPostiLiberi(postiLiberi);
        Posto posto = new Posto(1);


        // Mock behavior
        when(mockSpiaggiaDAO.trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class))).thenReturn(spiaggia);
        when(prenotazioneDAO.nuovaPrenotazione(anyInt())).thenReturn(new Prenotazione());
        when(postoDAO.getPosto(anyInt())).thenReturn(posto);

        CountDownLatch latch = new CountDownLatch(1);
        // Set data inizio
        clickOn("#dataInizio").write(tomorrow.toString()).type(KeyCode.ENTER);
        clickOn("#dataFine").type(KeyCode.END).eraseText(10);
        clickOn("#dataFine").write(tomorrow.plusDays(1).toString());
        clickOn("#confermaDate");

        // Add a seat
        clickOn("#numeroPosto").write("1");
        clickOn("#aggiungiPosto");

        // Verify that the seat is added to the reservation summary
        TreeItem<String> root = controller.getRiepilogoPrenotazione().getRoot();
        assertEquals(1, root.getChildren().size());
        assertEquals("Posto numero: 1", root.getChildren().get(0).getValue());

        latch.countDown();

        latch.await(5, TimeUnit.SECONDS);

        // Verify interactions
        verify(postoDAO, times(1)).getPosto(anyInt());
    }

    @Test
    public void testAggiungiAttrezzatura() throws InterruptedException {
        // Mock data
        List<Integer> postiLiberi = new ArrayList<>();
        postiLiberi.add(1);
        postiLiberi.add(2);
        Spiaggia spiaggia = new Spiaggia("");
        spiaggia.setPostiLiberi(postiLiberi);
        Posto posto = new Posto(1);

        // Mock behavior
        when(mockSpiaggiaDAO.trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class))).thenReturn(spiaggia);
        when(prenotazioneDAO.nuovaPrenotazione(anyInt())).thenReturn(new Prenotazione());
        when(postoDAO.getPosto(anyInt())).thenReturn(posto);

        CountDownLatch latch = new CountDownLatch(1);

        // Set data inizio
        sleep(500);
        clickOn("#dataInizio").write(tomorrow.toString()).type(KeyCode.ENTER);
        sleep(500);
        clickOn("#dataFine").type(KeyCode.END).eraseText(10);
        clickOn("#dataFine").write(tomorrow.plusDays(1).toString());

        clickOn("#confermaDate");

        // Add a seat
        clickOn("#numeroPosto").write("1");
        clickOn("#aggiungiPosto");

        // Select the seat
        controller.getRiepilogoPrenotazione().getRoot().setExpanded(true);
        sleep(500);
        clickOn(controller.getRiepilogoPrenotazione().getRoot().getChildren().get(0).getValue());

        // Add equipment
        clickOn("#aggiungiAttrezzatura");

        // Verify that the equipment is added to the selected seat
        TreeItem<String> seatItem = controller.getRiepilogoPrenotazione().getRoot().getChildren().get(0);
        assertEquals(1, seatItem.getChildren().size());
        assertEquals("Lettino", seatItem.getChildren().get(0).getValue());
        latch.countDown();
        latch.await(5, TimeUnit.SECONDS);

        // Verify interactions
        verify(postoDAO, times(1)).getPosto(anyInt());
    }

    @Test
    public void testConfermaPrenotazione() throws InterruptedException {
        // Mock data
        List<Integer> postiLiberi = new ArrayList<>();
        postiLiberi.add(1);
        postiLiberi.add(2);
        Spiaggia spiaggia = new Spiaggia("");
        spiaggia.setPostiLiberi(postiLiberi);
        Posto posto = new Posto(1);
        Prenotazione prenotazione = new Prenotazione();

        // Mock behavior
        when(mockSpiaggiaDAO.trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class))).thenReturn(spiaggia);
        when(prenotazioneDAO.nuovaPrenotazione(anyInt())).thenReturn(prenotazione);
        when(postoDAO.getPosto(anyInt())).thenReturn(posto);
        doNothing().when(prenotazioneDAO).addPrenotazione(any(Prenotazione.class));

        CountDownLatch latch = new CountDownLatch(1);

        // Set data inizio
        sleep(1000);
        clickOn("#dataInizio");
        String date = tomorrow.toString();
        write(date);
        type(KeyCode.ENTER);
        sleep(1000);
        date = tomorrow.plusDays(1).toString();
        clickOn("#dataFine").type(KeyCode.END).eraseText(10);
        write(date);
        type(KeyCode.ENTER);
        sleep(1000);
        clickOn("#confermaDate");

        // Add a seat
        clickOn("#numeroPosto").write("1");
        clickOn("#aggiungiPosto");

        // Confirm the reservation
        clickOn("#confermaPostazione");

        // Verify that the reservation is confirmed and the data is reset
        assertNull(controller.getDataInizio().getValue());
        assertNull(controller.getDataFine().getValue());
        assertTrue(controller.getPostiLiberi().getItems().isEmpty());
        assertTrue(controller.getMapper().isEmpty());
        assertTrue(controller.getRiepilogoPrenotazione().getRoot().getChildren().isEmpty());
        assertEquals("€ 0,00", controller.getPrezzo().getText());


        latch.await(5, TimeUnit.SECONDS);

        // Verify interactions
        verify(postoDAO, times(1)).getPosto(anyInt());
        verify(prenotazioneDAO, times(1)).addPrenotazione(any(Prenotazione.class));
    }

    @Test
    public void testEliminaPostazione() throws InterruptedException {
        // Mock data
        List<Integer> postiLiberi = new ArrayList<>();
        postiLiberi.add(1);
        postiLiberi.add(2);
        Spiaggia spiaggia = new Spiaggia("");
        spiaggia.setPostiLiberi(postiLiberi);
        Posto posto = new Posto(1);

        // Mock behavior
        when(mockSpiaggiaDAO.trovaPostiLiberi(any(LocalDate.class), any(LocalDate.class))).thenReturn(spiaggia);
        when(prenotazioneDAO.nuovaPrenotazione(anyInt())).thenReturn(new Prenotazione());
        when(postoDAO.getPosto(anyInt())).thenReturn(posto);

        CountDownLatch latch = new CountDownLatch(1);
        // Set data inizio
        clickOn("#dataInizio").write(tomorrow.toString()).type(KeyCode.ENTER);
        clickOn("#dataFine").type(KeyCode.END).eraseText(10);
        write(tomorrow.plusDays(1).toString());
//            type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#confermaDate");

        // Add a seat
        clickOn("#numeroPosto").write("1");
        clickOn("#aggiungiPosto");

        // Select the seat
        controller.getRiepilogoPrenotazione().getRoot().setExpanded(true);
        sleep(500);
        rightClickOn(controller.getRiepilogoPrenotazione().getRoot().getChildren().get(0).getValue());

        // Delete the seat
        clickOn("#eliminaPostazione");

        // Verify that the seat is removed from the reservation summary
        TreeItem<String> root = controller.getRiepilogoPrenotazione().getRoot();
        assertTrue(root.getChildren().isEmpty());

        // Verify that the seat is added back to the available seats list
        assertEquals(2, controller.getPosti().size());
        assertTrue(controller.getPosti().contains(1));

        latch.countDown();

        latch.await(5, TimeUnit.SECONDS);

        // Verify interactions
        verify(postoDAO, times(1)).getPosto(anyInt());
    }

    @Test
    public void testMyTextField() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        String date = tomorrow.toString();
        sleep(500);

        clickOn("#numeroPosto");
//          type(KeyCode.TAB); // Try adding a TAB for focus
        write(date);
        sleep(1000);
        type(KeyCode.Y, KeyCode.O, KeyCode.U, KeyCode.R, KeyCode.SPACE, KeyCode.T, KeyCode.E, KeyCode.X, KeyCode.T);
        sleep(500);
    }

}