package ORM;

import DomainModel.Posto;

public class PostoDAO {


    public Posto getPosto(int n) {
        Posto p = new Posto(n);
        //TODO imposta la fila di p
        return p;
    }
}
