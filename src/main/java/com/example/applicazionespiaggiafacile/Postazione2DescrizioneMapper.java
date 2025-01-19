package com.example.applicazionespiaggiafacile;

import java.util.Map;

public class Postazione2DescrizioneMapper {
    Map<String, Integer> mapper = new java.util.HashMap<>();

    public void aggiungi(int id, String descrizione) {
        mapper.put(descrizione, id);
    }
    public void rimuovi(int id) {
        mapper.remove(id);
    }

    public int get(String descrizione) {
        return mapper.get(descrizione);
    }
    public void clear() {
        mapper.clear();
    }

    boolean isEmpty() {
        return mapper.isEmpty();
    }
}