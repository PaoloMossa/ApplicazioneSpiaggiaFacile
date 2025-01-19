package DomainModel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostazioneTest {

    @Test
    public void testGetPrezzo() {
        Postazione postazione = new Postazione(new Posto(1));
        postazione.aggiungiAttrezzatura(new SediaRegista());
        postazione.setOmbrellone(true);

        //Il prezzo totale dovrebbe essere 30: 10 per il posto, 15 per l'ombrellone, 5 per la sedia
        assertEquals(30, postazione.getPrezzo(), 0.001);
    }
}