package DomainModel;

public class Posto {
    private int fila;
    private int numero;

    public Posto( int numero) {
        this.numero = numero;

        //TODO sostituire il seguente con una funione che determina la fila
        fila = (numero + 9) / 10;
    }
    public int getFila() {
        return fila;
    }
    public int getNumero() {
        return numero;
    }
    //TODO Aggiungere un metodo che ritorni la posizione, x=numero e y=fila

    double getPrezzo() {
        return 10;
    }
}