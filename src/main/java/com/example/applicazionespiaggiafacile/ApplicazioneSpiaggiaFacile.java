package com.example.applicazionespiaggiafacile;

import ORM.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class ApplicazioneSpiaggiaFacile extends Application {
    @Override
    public void start(Stage stage) {

        try {

            System.out.println(getClass().getResource("paginaUtente.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicazioneSpiaggiaFacile.class.getResource("paginaUtente.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("Spiaggia Facile");
            stage.setScene(scene);

            //Viene dato al Controller dell'identificativo dell'Utente attuale
            System.out.println("Inserire qua sotto il numero dell'Utente:");
            Scanner s = new Scanner(System.in);
            int idUtente = s.nextInt();
            PaginaUtenteController c = (PaginaUtenteController) fxmlLoader.getController();
            c.setIdUtente(idUtente);

            //Conferma di chiusura
            stage.setOnCloseRequest(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conferma");
                alert.setHeaderText("Conferma di chiusura");
                alert.setContentText("Sei sicuro di voler chiudere l'applicazione?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    try {
//                        c.confermaPostazione(new ActionEvent()); TODO cancella questa riga
                        DatabaseConnection.closeConnection();
                    }
                    catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    stage.close();
                } else {
                    e.consume();
                }
            });


            stage.show();

        }
        catch (IOException e) {
            // Mostra un avviso in caso di errore
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore di caricamento");
            alert.setContentText("Non è stato possibile caricare il file FXML: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {launch();}
}