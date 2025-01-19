package com.example.applicazionespiaggiafacile;

import DomainModel.*;
import ORM.PostoDAO;
import ORM.PrenotazioneDAO;
import ORM.SpiaggiaDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PaginaUtenteController implements Initializable {

    private int idUtente;
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
        this.prenotazione = prenotazioneDAO.nuovaPrenotazione(idUtente);
    }
    public int getIdUtente() {
        return idUtente;
    }

    //DAOs
    private SpiaggiaDAO spiaggiaDAO = new SpiaggiaDAO();
    private PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
    private PostoDAO postoDAO = new PostoDAO();
    //Lista di posti liberi
    private ObservableList<Integer> posti = FXCollections.observableArrayList();
    //Prenotazione, viene inviata per essere salvata
    private Prenotazione prenotazione;

    //Per facilitare la visualizzazione in stringhe, è usato una sorta di "mapper", che mantiene la corrispondenza
    // tra gli oggetti e la loro descrizione nela lista
    private Map<Integer, Postazione> postazioni = new HashMap<>();
    private Postazione2DescrizioneMapper mapper = new Postazione2DescrizioneMapper();


    @FXML
    private DatePicker dataFine;

    @FXML
    private DatePicker dataInizio;

    @FXML
    private TextField numeroPosto;

    @FXML
    private ListView<Integer> postiLiberi = new ListView<>(posti);

    @FXML
    private TreeView<String> riepilogoPrenotazione;

    @FXML
    private ChoiceBox<Attrezzatura> selettoreAttrezzaura;

    @FXML
    private CheckBox selezioneOmbrellone;

    @FXML
    private Spinner<Integer> spinnerAttrezzatura;

    @FXML
    private Label prezzo;

    //Per il registro delle prenotazioni
    @FXML
    private ChoiceBox<String> selettoreRegistro;
    @FXML
    private Button reg_elim_pren;
    @FXML
    private Button reg_mod_pren;
    @FXML
    private AnchorPane modificaDataPane;
    @FXML
    private DatePicker dataFine_modificata;

    private ObservableList<Prenotazione> reg_prenotazioni = FXCollections.observableArrayList();

    @FXML
    private TableView<Prenotazione> registroPrenotazioni;
    @FXML
    private TableColumn<Prenotazione, Integer> reg_tab_id;
    @FXML
    private TableColumn<Prenotazione, LocalDate> reg_tab_da;
    @FXML
    private TableColumn<Prenotazione, LocalDate> reg_tab_a;
    @FXML
    private TableColumn<Prenotazione, String> reg_tab_prezzo;
    @FXML
    private TableColumn<Prenotazione, Integer> reg_tab_cliente;

    public SpiaggiaDAO getSpiaggiaDAO() {
        return spiaggiaDAO;
    }

    public void setSpiaggiaDAO(SpiaggiaDAO spiaggiaDAO) {
        this.spiaggiaDAO = spiaggiaDAO;
    }

    public PrenotazioneDAO getPrenotazioneDAO() {
        return prenotazioneDAO;
    }

    public void setPrenotazioneDAO(PrenotazioneDAO prenotazioneDAO) {
        this.prenotazioneDAO = prenotazioneDAO;
    }

    public PostoDAO getPostoDAO() {
        return postoDAO;
    }

    public void setPostoDAO(PostoDAO postoDAO) {
        this.postoDAO = postoDAO;
    }

    public ObservableList<Integer> getPosti() {
        return posti;
    }

    public void setPosti(ObservableList<Integer> posti) {
        this.posti = posti;
    }

    public Postazione2DescrizioneMapper getMapper() {
        return mapper;
    }

    public void setMapper(Postazione2DescrizioneMapper mapper) {
        this.mapper = mapper;
    }


    public void setDataFine(DatePicker dataFine) {
        this.dataFine = dataFine;
    }

    public DatePicker getDataFine() {
        return dataFine;
    }

    public void setDataInizio(DatePicker dataInizio) {
        this.dataInizio = dataInizio;
    }

    public DatePicker getDataInizio() {
        return dataInizio;
    }

    public TextField getNumeroPosto() {
        return numeroPosto;
    }

    public void setNumeroPosto(TextField numeroPosto) {
        this.numeroPosto = numeroPosto;
    }

    public ListView<Integer> getPostiLiberi() {
        return postiLiberi;
    }

    public void setPostiLiberi(ListView<Integer> postiLiberi) {
        this.postiLiberi = postiLiberi;
    }

    public TreeView<String> getRiepilogoPrenotazione() {
        return riepilogoPrenotazione;
    }

    public void setRiepilogoPrenotazione(TreeView<String> riepilogoPrenotazione) {
        this.riepilogoPrenotazione = riepilogoPrenotazione;
    }

    public ChoiceBox<Attrezzatura> getSelettoreAttrezzaura() {
        return selettoreAttrezzaura;
    }

    public void setSelettoreAttrezzaura(ChoiceBox<Attrezzatura> selettoreAttrezzaura) {
        this.selettoreAttrezzaura = selettoreAttrezzaura;
    }

    public CheckBox getSelezioneOmbrellone() {
        return selezioneOmbrellone;
    }

    public void setSelezioneOmbrellone(CheckBox selezioneOmbrellone) {
        this.selezioneOmbrellone = selezioneOmbrellone;
    }

    public Spinner<Integer> getSpinnerAttrezzatura() {
        return spinnerAttrezzatura;
    }

    public void setSpinnerAttrezzatura(Spinner<Integer> spinnerAttrezzatura) {
        this.spinnerAttrezzatura = spinnerAttrezzatura;
    }

    public Label getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Label prezzo) {
        this.prezzo = prezzo;
    }

    public ChoiceBox<String> getSelettoreRegistro() {
        return selettoreRegistro;
    }

    public void setSelettoreRegistro(ChoiceBox<String> selettoreRegistro) {
        this.selettoreRegistro = selettoreRegistro;
    }

    public Button getReg_elim_pren() {
        return reg_elim_pren;
    }

    public void setReg_elim_pren(Button reg_elim_pren) {
        this.reg_elim_pren = reg_elim_pren;
    }

    public Button getReg_mod_pren() {
        return reg_mod_pren;
    }

    public void setReg_mod_pren(Button reg_mod_pren) {
        this.reg_mod_pren = reg_mod_pren;
    }

    public AnchorPane getModificaDataPane() {
        return modificaDataPane;
    }

    public void setModificaDataPane(AnchorPane modificaDataPane) {
        this.modificaDataPane = modificaDataPane;
    }

    public DatePicker getDataFine_modificata() {
        return dataFine_modificata;
    }

    public void setDataFine_modificata(DatePicker dataFine_modificata) {
        this.dataFine_modificata = dataFine_modificata;
    }

    public ObservableList<Prenotazione> getReg_prenotazioni() {
        return reg_prenotazioni;
    }

    public void setReg_prenotazioni(ObservableList<Prenotazione> reg_prenotazioni) {
        this.reg_prenotazioni = reg_prenotazioni;
    }

    public TableView<Prenotazione> getRegistroPrenotazioni() {
        return registroPrenotazioni;
    }

    public void setRegistroPrenotazioni(TableView<Prenotazione> registroPrenotazioni) {
        this.registroPrenotazioni = registroPrenotazioni;
    }

    public TableColumn<Prenotazione, Integer> getReg_tab_id() {
        return reg_tab_id;
    }

    public void setReg_tab_id(TableColumn<Prenotazione, Integer> reg_tab_id) {
        this.reg_tab_id = reg_tab_id;
    }

    public TableColumn<Prenotazione, LocalDate> getReg_tab_da() {
        return reg_tab_da;
    }

    public void setReg_tab_da(TableColumn<Prenotazione, LocalDate> reg_tab_da) {
        this.reg_tab_da = reg_tab_da;
    }

    public TableColumn<Prenotazione, LocalDate> getReg_tab_a() {
        return reg_tab_a;
    }

    public void setReg_tab_a(TableColumn<Prenotazione, LocalDate> reg_tab_a) {
        this.reg_tab_a = reg_tab_a;
    }

    public TableColumn<Prenotazione, String> getReg_tab_prezzo() {
        return reg_tab_prezzo;
    }

    public void setReg_tab_prezzo(TableColumn<Prenotazione, String> reg_tab_prezzo) {
        this.reg_tab_prezzo = reg_tab_prezzo;
    }

    public TableColumn<Prenotazione, Integer> getReg_tab_cliente() {
        return reg_tab_cliente;
    }

    public void setReg_tab_cliente(TableColumn<Prenotazione, Integer> reg_tab_cliente) {
        this.reg_tab_cliente = reg_tab_cliente;
    }

    public Map<Integer, Postazione> getPostazioni() {
        return postazioni;
    }

    public void setPostazioni(Map<Integer, Postazione> postazioni) {
        this.postazioni = postazioni;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    @FXML
    void aggiungiAttrezzatura(ActionEvent event) {

        //FIXME rivedi il commento di questa variabile
        //nel secondo if si controlla che l'elemento selezionato sia una postazione (e non un'attrezzatura o la radice
        //per evitare ridondanze viene creata la seguente variabile
        TreeItem<String> elementoSelezionato = riepilogoPrenotazione.getSelectionModel().getSelectedItem();

        //Occorre prima selezionare una postazione tra quelle scelte precedentemente, di seguito viene controllato
        if (riepilogoPrenotazione.getRoot().getChildren().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Selezionare prima una postazione");
            alert.show();
            return;
        }
        else if (elementoSelezionato == null || elementoSelezionato.getParent() != riepilogoPrenotazione.getRoot()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Selezionare la postazione in cui si desidera vengano aggiunte le attrezzature");
            alert.show();
            return;
        }
        else {
            Attrezzatura attrezzatura = selettoreAttrezzaura.getValue();
            int numTotaleAttrezzature = spinnerAttrezzatura.getValue();
            aggiungiTuttaAttrezzatura(numTotaleAttrezzature, elementoSelezionato, attrezzatura);

            aggiornaPrezzo();
        }


        System.out.println("Attrezzatura aggiunta");
    }

    private void aggiungiTuttaAttrezzatura(int numTotaleAttrezzature, TreeItem<String> elementoSelezionato, Attrezzatura attrezzatura) {
        for (int i = 0; i < numTotaleAttrezzature; i++) {
            elementoSelezionato.getChildren().add(new TreeItem<>(attrezzatura.toString()));
            String descr = elementoSelezionato.getValue();
            int id = mapper.get(descr);
            postazioni.get(id).aggiungiAttrezzatura(attrezzatura);
        }
    }


    @FXML
    void aggiungiPosto(ActionEvent event) {
        //Si assicura che sia stato inserito un valore legale
        boolean legale = false;

        //controlla il testo inserito
        if (!isPostoLegale()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Inserire un numero di posto valido");
            alert.show();
            return;
        }

        int numPosto = Integer.parseInt(numeroPosto.getText());

        String descrizione = visualizzaPostiScelti(numPosto);

        //Il posto aggiunto viene rimosso da quelli disponibili
        int i = posti.indexOf(numPosto);
        posti.remove(i);


        salvaNellaListaPostazioni(numPosto, descrizione);

        aggiornaPrezzo();

        //Si azzera il valore del campo di testo e del selettore per l'ombrellone
        selezioneOmbrellone.setSelected(false);
        numeroPosto.clear();
    }

    private void salvaNellaListaPostazioni(int numPosto, String descrizione) {
        //Salva il risultato in postazione
        postazioni.put(numPosto, new Postazione(postoDAO.getPosto(numPosto)));
        postazioni.get(numPosto).setOmbrellone(selezioneOmbrellone.isSelected());
        mapper.aggiungi(numPosto, descrizione); //descrizione è stato usato prima per aggiungere la stringa alla TreeView
        System.out.println("Postazione aggiunta");
    }

    private boolean isPostoLegale() {
        boolean legale = false;
        //Si cerca se il posto inserito sia presente nella lista dei posti liberi
        //passare come argomento di parseInt il valore null, genera una RuntimeException
        try {
            for (int i = 0; i < posti.size(); i++) {
                if (posti.get(i).equals(Integer.parseInt(numeroPosto.getText())))
                    legale = true;

            }
        }
        catch (RuntimeException e) {System.out.println("Non è stato inserito un numero di posto");}
        catch (Exception e) {System.out.println(e.getMessage());}

        return legale;
    }

    private String visualizzaPostiScelti(int numPosto) {
        //Viene creata una stringa, per indicare nella lista se è stato scelto l'ombrellone
        String ombrellone = "";
        if (selezioneOmbrellone.isSelected())
            ombrellone = " con ombrellone";

        //Aggiunge all'albero
        String descrizione = aggiungiPostoAdAlbero(numPosto, ombrellone);

        return descrizione;
    }

    private String aggiungiPostoAdAlbero(int numPosto, String ombrellone) {
        String descrizione = "Posto numero: " + numPosto + ombrellone;
        TreeItem<String> nuovoPosto = new TreeItem<>(descrizione);
        riepilogoPrenotazione.getRoot().getChildren().add(nuovoPosto);
        System.out.println("Posto aggiunto");
        return descrizione;
    }



    @FXML
    void confermaPostazione(ActionEvent event) {
        if(postazioni.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Inserire almeno una postazione");
            alert.show();
            return;
        }

        List<Postazione> p = new ArrayList<>();
        for (Postazione postazione : postazioni.values()) {
            p.add(postazione);
        }

        prenotazione.aggiungiPostazione(p);
//        prenotazione.setId(prenotazioneDAO.);


        prenotazioneDAO.addPrenotazione(prenotazione);

        System.out.println("Postazione confermata");

        dataInizio.setValue(null);
        dataFine.setValue(null);
        postazioni.clear();
        postiLiberi.getItems().clear();
        mapper.clear();
        riepilogoPrenotazione.getRoot().getChildren().clear();
        prenotazione = prenotazioneDAO.nuovaPrenotazione(idUtente);
        riepilogoPrenotazione.getRoot().setValue("Prenotazione numero: "+prenotazione.getId());
        prezzo.setText("€ 0,00");
        registroPrenotazioni.refresh();

        modificaDataPane.setVisible(false);
    }

    @FXML
    void impostaDataFine(ActionEvent event) {


        //Controlla se la data di fine sia stata inserita in seguito a quella di inizio e se la data di inizio è a lei antecedente

//        if (dataInizio.getValue() == null) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Errore");
//            alert.setHeaderText("Inserire prima una data di inizio");
//            alert.show();
//            dataFine.setValue(null);
//        }
//        else if (dataFine.getValue().isBefore(dataInizio.getValue())) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Errore");
//            alert.setHeaderText("Inserire una data di fine valida");
//            alert.show();
//            dataFine.setValue(null);
//            return;
//        }
//        else {
            System.out.println("Data fine impostata");
//        }
    }

    @FXML
    void impostaDataInizio(ActionEvent event) {

        //Imposta per predefinita la data odierna nel campo della data di fine
        if(dataFine.getValue() == null)
            dataFine.setValue(dataInizio.getValue());

        //Controlla che la data scelta sia nel futuro

//        try {
//            LocalDate d = dataInizio.getValue();
//            System.out.println(d);
//            if (d == null || d.isBefore(java.time.LocalDate.now())) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Errore");
//                alert.setHeaderText("Inserire una data di inizio valida");
//                alert.show();
//                dataInizio.setValue(null); //FIXME questa riga causa eccezione!!!
//                //event.consume(); //FIXME questo metodo è probabilmente inutile qua
//                return;
//            }
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//        }


        //Per verificare che tutto funzioni
        System.out.println(dataInizio.getValue());

        //In questa variabile sono salvati i posti liberi
//        Spiaggia sp;

        //ottiene i posti liberi
//        if (dataFine.getValue() == null) {
//            sp = spiaggiaDAO.trovaPostiLiberi(dataInizio.getValue());
//            System.out.println("sp contiene ora i posti liberi");
//        }
//        else {
//            sp = spiaggiaDAO.trovaPostiLiberi(dataInizio.getValue(), dataFine.getValue());
//        }
//
//        //aggiunge i posti disponibili alla lista in alto a sinistra
//        posti.clear();
//        List<Integer> i = sp.getPostiLiberi();
//        System.out.println("Posti liberi: " + i);
//        posti.addAll(i);
//

//        System.out.println("Data inizio impostata" + sp);

    }

    @FXML
    void scegliLettino(ActionEvent event) {
        selettoreAttrezzaura.show();
        System.out.println("Lettino scelto");
    }
    @FXML
    void scegliSedia(ActionEvent event) {
        System.out.println("Sedia scelta");
    }
    @FXML
    void scegliSdraio(ActionEvent event) {
        System.out.println("Sdraio scelta");
    }


    LocalDate daTestoAData(String text) {
        LocalDate data = null;
        try{
            data = LocalDate.parse(text);
        }
        catch(IllegalArgumentException | DateTimeParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Non è stata inserita una data valida da tastiera");
        }
        return data;
    }

    @FXML
    void confermaDate (ActionEvent event) {
        if(controllaDate()) {
            System.out.println("Date confermate");

            //Salva in sp i posti liberi per le date selezionate
            Spiaggia sp = spiaggiaDAO.trovaPostiLiberi(dataInizio.getValue(), dataFine.getValue());//FIXME inserire la funzione con due argomenti e rimuovere quella con uno solo dal DAO
            System.out.println("sp contiene ora i posti liberi");

            visualizzaPostiLiberi(sp);

            azzeraPrenotazione();

            //imposta la data in prenotazione
            prenotazione.setDate(dataInizio.getValue(), dataFine.getValue());

            System.out.println("sp contiene ora i posti liberi");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Le date inserite non sono valide");
            alert.show();


            System.out.println("Date non confermate");
        }
    }

    private void visualizzaPostiLiberi(Spiaggia sp) {
        //aggiunge i posti disponibili alla lista in alto a sinistra
        posti.clear();
        List<Integer> i = sp.getPostiLiberi();
        System.out.println("Posti liberi: " + i);
        posti.addAll(i);
    }

    private void azzeraPrenotazione() {
        //Se il cliente cambia idea e sceglie un'altra data dopo aver già selezionato un posto, le scelte precedenti sono cancellate
        riepilogoPrenotazione.getRoot().getChildren().clear();
        numeroPosto.clear();
        selezioneOmbrellone.setSelected(false);
        postazioni.clear();
        prezzo.setText("€ 0,00");

        int id = prenotazione.getId();
        int idCliente = prenotazione.getCliente().getIdentificativo();
        prenotazione = new Prenotazione();
        prenotazione.setId(id);
        prenotazione.getCliente().setIdentificativo(idCliente);

        mapper.clear();
    }

    private boolean controllaDate() {
        boolean legale = false;

        //Controlla che ci siano date inserite
        if(dataInizio.getValue() == null || dataFine.getValue() == null){
            System.out.println("Date non inserite");
        }

        //Controlla se le date sono valide: devono essere future e l'inizio deve essere antecedente alla fine
        else if(dataInizio.getValue().isBefore(LocalDate.now()) || dataFine.getValue().isBefore(dataInizio.getValue())){
            System.out.println("Date non valide");
        }
        else
            legale = true;

        return legale;
    }

    //TODO Parte eccezione se il campo di testo numero posto è vuoto
    //TODO Il prezzo non si azzera quando viene cambiata la data
    void aggiornaPrezzo() {
        double prezzoTotale = 0;
        for (Postazione postazione : postazioni.values()) {
            prezzoTotale += postazione.getPrezzo();
        }
        double giorni = ChronoUnit.DAYS.between(dataInizio.getValue(), dataFine.getValue());
        prezzoTotale = (giorni+1)*(prezzoTotale);
        System.out.println("Prezzo totale: " + prezzoTotale);

        //Il testo è formattato come di consueto in Italia
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ITALY);
        df.applyPattern("#,##0.00");
        prezzo.setText("€  " + df.format(prezzoTotale));
    }


    public void eliminaPostazione(ActionEvent event) {
        boolean legale = false;
        for (TreeItem<String> postoScelto : riepilogoPrenotazione.getRoot().getChildren()) {
            if (postoScelto == riepilogoPrenotazione.getSelectionModel().getSelectedItem())
                legale = true;
        }

        if (!legale) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Selezionare una Postazione");
            alert.show();
            System.out.println("Postazione non aggiornata");
            return;
        }

        int postoDaRimuovere = mapper.get(riepilogoPrenotazione.getSelectionModel().getSelectedItem().getValue());
        posti.add(postoDaRimuovere);
        posti.sort(Comparator.naturalOrder());

        postazioni.remove(postoDaRimuovere);
        mapper.rimuovi(postoDaRimuovere);
        riepilogoPrenotazione.getRoot().getChildren().remove(riepilogoPrenotazione.getSelectionModel().getSelectedItem());

        aggiornaPrezzo();
    }


    public void eliminaDaRegistro (ActionEvent event) {
        int id = registroPrenotazioni.getSelectionModel().getSelectedItem().getId();

        prenotazioneDAO.rimuoviPostazione(id);
        System.out.println("Prenotazione numero "+id+" eliminata da registro");

        prezzo.setText("€ 0,00");
    }

    public void modificaDaRegistro (ActionEvent event) {

        try {
            int codPrenotazione = registroPrenotazioni.getSelectionModel().getSelectedItem().getId();
            int codCliente = registroPrenotazioni.getSelectionModel().getSelectedItem().getCliente().getIdentificativo();


            azzeraPrenotazione();
            prenotazione = prenotazioneDAO.getPrenotazione(codPrenotazione, codCliente);

            riepilogoPrenotazione.getRoot().setValue("Prenotazione numero: " + prenotazione.getId());


            //Se la prenotazione è già cominciata, non è possibile cambiare la data di inizio
            if (selettoreRegistro.getSelectionModel().getSelectedIndex() == 1) {
                modificaDataPane.setVisible(true);
                dataFine_modificata.setValue(prenotazione.getDataFine());
            }


            //FIXME queste tre righe sembrano strane...
            dataInizio.setValue(prenotazione.getDataInizio());
            dataFine.setValue(prenotazione.getDataFine());
//            prenotazione.setDate(dataInizio.getValue(), dataFine.getValue());

            //Salva in sp i posti liberi per le date selezionate
            Spiaggia sp = spiaggiaDAO.trovaPostiLiberi(dataInizio.getValue(), dataFine.getValue());
            System.out.println("sp contiene ora i posti liberi");

            visualizzaPostiLiberi(sp);

            postazioni.clear();

            //FIXME il commento sotto è stato risolto, tutto è fatto all'interno del metodo (?)
            //Per visualizzare le postazioni scelte, occorre aggiornare la mappa
//            for (Postazione p : prenotazione.getPostazioni())
//                postazioni.put(p.getPosto().getNumero(),p);

            aggiungiPostiPerModifica();

            aggiornaPrezzo();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("La prenotazione non esiste");
            alert.show();
            System.out.println("Prenotazione non selezionata");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void modificaDataFine (ActionEvent event) {

        LocalDate valorePrecedente = dataFine.getValue();
        dataFine.setValue(dataFine_modificata.getValue());

        if (dataFine_modificata == null || dataFine_modificata.getValue().isBefore(LocalDate.now())) {
            dataFine.setValue(valorePrecedente);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Le date inserite non sono valide");
            alert.show();
            return;
        }

        //Completa le liste, visualizza i posti e ripristina la bozza della prenotazione, come se fosse in corso
        aggiungiPostiPerModifica();

        aggiornaPrezzo();
    }

    private void aggiungiPostiPerModifica () {
        postazioni.clear();

        for (Postazione p : prenotazione.getPostazioni()) {
            int numPosto;
            Postazione postazione = p;
            numPosto = p.getPosto().getNumero();

            if (postazione.isOmbrellone())
                selezioneOmbrellone.setSelected(true); //visualizzaPostiScelti utilizza il selettore per vedere se l'ombrellone è stato scelto

            //Permette di visualizzare le proprie scelte in alto a destra
            String descrizione = visualizzaPostiScelti(numPosto);

            salvaNellaListaPostazioni(numPosto, descrizione); //Aggiorna le variabili mapper e postazioni

            //Vengono aggiunte le attrezzature presenti nella prenotazione da modificare
            TreeItem<String> elementoSelezionato = getElementoSelezionato(descrizione);

            //Nel caso prenotazione sia una preesistente, una volta ripristinata come "in corso", quindi con la lista di postazioni vuota
            prenotazione.clearPostazioni();

            for (Attrezzatura a : postazione.getAttrezzature()) {
                aggiungiTuttaAttrezzatura(1, elementoSelezionato, a);
            }


            selezioneOmbrellone.setSelected(false);
        }

        aggiornaPrezzo();
    }

    private TreeItem<String> getElementoSelezionato(String descrizione) {
        TreeItem<String> risultato = null;

        for (TreeItem<String> e : riepilogoPrenotazione.getRoot().getChildren()) {
            if (e.getValue().equals(descrizione)) {
                risultato = e;
            }
        }

        return risultato;
    }

    @FXML
    void vaiAPaginaRichieste(ActionEvent event) {
        //TODO CITA CONVERSAZIONE GEMINI 25 DIC: "Open new Window..."
        // Load the FXML file for the new window (page2.fxml)

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paginaRichieste.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Errore nell'apertura pagina richieste");
        }

        // Get the controller for the new window (Page2Controller)
        PaginaRichiesteController paginaRichiesteController = fxmlLoader.getController();

        // You can now initialize the controller if needed.
        // Pass data or perform setup here. For example:
        paginaRichiesteController.setIdCliente(this.idUtente);

        // Create a new stage (window)
        Stage stage = new Stage();
        stage.setTitle("Pagina Richieste"); // Set the title of the new window
        stage.setScene(new Scene(root));

        // Optionally, make the new window a modal window
        // This prevents interaction with the main window until the new window is closed
        stage.initModality(Modality.APPLICATION_MODAL);


        // Show the new window
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Per vedere che succede. La lista dovrebbe aggiornarsi anche senza questa parte
        posti.addListener((ListChangeListener<Integer>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println("Items added: " + c.getAddedSubList());
                    // No need to explicitly update the ListView. It updates automatically.
                } else if (c.wasRemoved()) {
                    System.out.println("Items removed: " + c.getRemoved());
                } else if (c.wasUpdated()) {
                    System.out.println("Items updated");
                }
            }
        });

        postiLiberi.setItems(posti);

        //Inizializza il riepilogo della prenotazione
        prenotazione = prenotazioneDAO.nuovaPrenotazione(this.idUtente); //FIXME la prenotazione è inizializzata quando viene fornito l'id non prima
        riepilogoPrenotazione.setRoot(new TreeItem<>("Prenotazione numero: " + prenotazione.getId()));


        //Occorre inizializzare lo Spinner noto come spinnerAttrezzatura
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
        valueFactory.setValue(1);
        spinnerAttrezzatura.setValueFactory(valueFactory);

        //Occorre inizializzare il selettore di attrezzature
        List<Attrezzatura> attrezzature = new ArrayList<>();
        attrezzature.add(new Lettino());
        attrezzature.add(new Sdraio());
        attrezzature.add(new SediaRegista());
        selettoreAttrezzaura.getItems().addAll(attrezzature);
        selettoreAttrezzaura.getSelectionModel().selectFirst();


        //Vengono inizializzate le colonne e associate agli attributi di Postazione
        reg_tab_id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        reg_tab_id.setSortType(TableColumn.SortType.DESCENDING);
        reg_tab_da.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDataInizio()));
        reg_tab_a.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDataFine()));

        //formattazione del prezzo
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ITALY);
        df.applyPattern("#,##0.00");
        reg_tab_prezzo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(df.format(cellData.getValue().getPrezzo())));

        reg_tab_cliente.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCliente().getIdentificativo()));

        //La lista osservabile è aggiunta alla tabella
        registroPrenotazioni.setItems(reg_prenotazioni);
        registroPrenotazioni.getSortOrder().add(reg_tab_id);
        registroPrenotazioni.sort();

        //Se a effettuare l'accesso non è stato il gestore (il cui id è 0), non occorre che la colonna cliente sia visibile
        if (idUtente != 0)
            reg_tab_cliente.setVisible(false);

        //Viene inizializzato il selettore del Registro delle Prenotazioni
        //Attraverso esso si decide se visualizzare le prenotazioni per date passate o future
        List<String> opzioni = new ArrayList<>();
        opzioni.add("Prenotazioni future");
        opzioni.add("Prenotazioni correnti");
        opzioni.add("Prenotazioni passate");
        selettoreRegistro.getItems().addAll(opzioni);

        //I pulsanti Elimina e Modifica sono inizialmente disabilitati
        reg_elim_pren.setDisable(true);
        reg_mod_pren.setDisable(true);


        //Aggiornamento automatico della tabella
        selettoreRegistro.getSelectionModel().selectedIndexProperty().
                addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                System.out.println("Date future");

                reg_elim_pren.setDisable(false);
                reg_mod_pren.setDisable(false);
                List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniFuture(idUtente);
                reg_prenotazioni.clear();
                reg_prenotazioni.addAll(prenotazioni);
            }
            else if (newValue.intValue() == 2) {
                System.out.println("Date passate");

                reg_elim_pren.setDisable(true);
                reg_mod_pren.setDisable(true);
                List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniPassate(idUtente);

                reg_prenotazioni.clear();
                reg_prenotazioni.addAll(prenotazioni);
                System.out.println("" + reg_prenotazioni.size());
            }
            else if (newValue.intValue() == 1) {
                System.out.println("Date correnti");

                reg_elim_pren.setDisable(true);
                reg_mod_pren.setDisable(false);

                reg_prenotazioni.clear();
                List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniCorrenti(idUtente);
                reg_prenotazioni.addAll(prenotazioni);
            }
            registroPrenotazioni.sort();
        });

        dataInizio.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            LocalDate data = null;
            if (event.getCode() == KeyCode.ENTER)
                data = daTestoAData(dataInizio.getEditor().getText());
            if (data != null)
                dataInizio.setValue(data);
        });

        dataFine.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            LocalDate data = null;
            if (event.getCode() == KeyCode.ENTER)
                data = daTestoAData(dataFine.getEditor().getText());
            if (data != null)
                dataFine.setValue(data);
        });

    }

}
