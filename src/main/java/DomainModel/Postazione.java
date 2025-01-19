package DomainModel;

import java.util.ArrayList;
import java.util.List;

public class Postazione {

    private boolean ombrellone;
    private Posto posto;
    private List<Attrezzatura> attrezzature;

    public Postazione(Posto posto) {
        this.posto = posto;
        attrezzature = new ArrayList<>();
    }

    public Postazione(Postazione postazione) {
        this.posto = postazione.posto;
        this.attrezzature = new ArrayList<>(postazione.attrezzature);
        this.ombrellone = postazione.ombrellone;
    }

    public void aggiungiAttrezzatura(Attrezzatura a){
        attrezzature.add(a);
    }
    public void aggiungiAttrezzatura(List<Attrezzatura> a){
        attrezzature.addAll(a);
    }

    public void rimuoviAttrezzatura(Attrezzatura a){
        attrezzature.remove(a);
    }

    public boolean isOmbrellone() {
        return ombrellone;
    }
    public void setOmbrellone(boolean ombrellone) {
        this.ombrellone = ombrellone;
        System.out.println("Ombrellone: " + ombrellone);
    }

    public Posto getPosto() {
        return posto;
    }

    public List<Attrezzatura> getAttrezzature() {
        return attrezzature;
    }

    public double getPrezzo() {
        double totale = 0.0;
        if (ombrellone)
            totale += PrezziServizi.PREZZO_OMBRELLONE.getPrezzo();
        for (Attrezzatura a : attrezzature)
            totale += a.getPrezzo();
        return (totale + this.posto.getPrezzo());
    }
}

