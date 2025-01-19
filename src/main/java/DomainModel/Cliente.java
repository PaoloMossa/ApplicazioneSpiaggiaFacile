package DomainModel;

public class Cliente {
    private String nome;
    private String cognome;
    private int identificativo;
    private int indiceFedeltà;

    public Cliente() {}

    public Cliente(String nome, String cognome, int identificativo) {
        this.nome = nome;
        this.cognome = cognome;
        this.identificativo = identificativo;
    }

    public Cliente (Cliente c) {
        this.nome = c.getNome();
        this.cognome = c.getCognome();
        this.identificativo = c.getIdentificativo();
        this.indiceFedeltà = c.getIndiceFedeltà();
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public int getIdentificativo() {
        return identificativo;
    }
    public void setIndiceFedeltà(int i) {
        this.indiceFedeltà = i;
    }
    public int getIndiceFedeltà() {
        return indiceFedeltà;
    }

    public void setIdentificativo(int identificativo) {
        this.identificativo = identificativo;
    }

    public void aumentaIndiceFedeltà (int valore) {
        if (indiceFedeltà + valore > 0)
            indiceFedeltà += valore;
    }

    public String toString() {
        return "" + identificativo;
    }
}
