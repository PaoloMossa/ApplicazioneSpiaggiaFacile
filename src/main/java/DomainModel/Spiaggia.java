package DomainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spiaggia {
    private String nome;
    private Map<Integer, Boolean> postiLiberi;


    public Spiaggia(String nome) {
        this.nome = nome;
        postiLiberi = new HashMap<>();
        for (int i = 0; i < 50; i++) //FIXME il numero di posti totale deve essere ottenuto da postoDAO o da un metodo di SpiaggiaDAO
            this.aggiungiPosto(i);
    }

    public Spiaggia(Spiaggia spiaggia) {
        this.nome = spiaggia.nome;
        this.postiLiberi = new HashMap<>();
        for (int i = 0; i < 50; i++)
            this.aggiungiPosto(i); //FIXME il numero di posti totale deve essere ottenuto da postoDAO o da un metodo di SpiaggiaDAO
        for (Integer num : spiaggia.getPostiLiberi())
            this.postiLiberi.replace(num, true);
    }

    public void occupaPosto(int numero) {
        postiLiberi.put(numero, false);
    }
    public void liberaPosto(int numero) {
        postiLiberi.replace(numero, true);
        System.out.println("Il posto "+numero+" è ora libero");
    }

    public void setPostiLiberi(List<Integer> posti) {
        for (Integer num : posti) {
            liberaPosto(num.intValue());
        }
    }

    public void aggiungiPosto(int numero) {
        postiLiberi.put(numero, false);
    }

    public List<Integer> getPostiLiberi() {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < postiLiberi.size(); i++) {
            if(postiLiberi.get(i))
                result.add(i);
        }
        return result;
    }
}