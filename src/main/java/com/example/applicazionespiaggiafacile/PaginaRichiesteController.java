package com.example.applicazionespiaggiafacile;

import DomainModel.Richiesta;
import ORM.ClienteDAO;
import ORM.RichiestaDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PaginaRichiesteController implements Initializable {

    int idCliente;
    ObservableList<Richiesta> listaRichieste;
    RichiestaDAO richiestaDAO = new RichiestaDAO();
    ClienteDAO clienteDAO = new ClienteDAO();

    @FXML
    private TextArea casellaRichiesta;

    @FXML
    private Label nomeCliente;

    @FXML
    private TableView<Richiesta> tabellaRichieste;

    @FXML
    private AnchorPane pag_nuovaRichiesta;

    @FXML
    private AnchorPane pag_riepilogoRichieste;

    @FXML
    private TableColumn<Richiesta, Integer> tab_cliente;

    @FXML
    private TableColumn<Richiesta, String> tab_richiesta;

    @FXML
    void inviaRichiesta(ActionEvent event) {
        if (casellaRichiesta.getText().trim().equals("")) { //Trim rimuove gli spazi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Non è stata digitata alcuna richiesta");
            alert.show();
            return;
        }
        String testo = casellaRichiesta.getText();
        richiestaDAO.nuovaRichiesta(testo, idCliente);
    }

    @FXML
    void nuovaRichiesta(ActionEvent event) {
        pag_nuovaRichiesta.setVisible(true);
        pag_riepilogoRichieste.setVisible(false);
    }

    @FXML
    void visualizzaRichieste(ActionEvent event) {
        pag_riepilogoRichieste.setVisible(true);
        pag_nuovaRichiesta.setVisible(false);
        listaRichieste.clear();
        listaRichieste.addAll(richiestaDAO.ottieniRichieste(idCliente));
    }

    @FXML
    void eseguiRichiesta(ActionEvent event) {
        int id = tabellaRichieste.getSelectionModel().getSelectedItem().getId();
        listaRichieste.removeIf(r -> r.getId() == id);
//        listaRichieste.notifyAll();
        richiestaDAO.eseguiRichiesta(id);
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
        String nome = clienteDAO.getCliente(idCliente).getNome() + " " + clienteDAO.getCliente(idCliente).getCognome();
        nomeCliente.setText(nome);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaRichieste = FXCollections.observableArrayList();

        //TODO inizializza la tabella, che deve seguire la observable list
        tabellaRichieste.setItems(listaRichieste);
        tabellaRichieste.refresh();


        tab_cliente.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCodiceCliente()));
        tab_richiesta.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTesto()));

        //TODO menziona nella relazione: Grazie ChatGPT 25-12-2024
        tab_richiesta.setCellFactory(column -> {
            return new TableCell<Richiesta, String>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(tab_richiesta.widthProperty().subtract(10)); // Adjust for padding
                        setGraphic(text);
                    }
                }
            };
        });


    }
}
