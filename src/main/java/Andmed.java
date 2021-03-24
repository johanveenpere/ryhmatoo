public class Andmed {
    private String viit;
    private float kaal;
    private String sugu;
    private int sünniaasta;

    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Andmed(String viit, float kaal) {
        this.viit = viit;
        this.kaal = kaal;
    }

    public String getViit() {
        return viit;
    }

    public void setViit(String viit) {
        this.viit = viit;
    }

    public float getKaal() {
        return kaal;
    }

    public void setKaal(float kaal) {
        this.kaal = kaal;
    }

    public String getSugu() {
        return sugu;
    }

    public void setSugu(String sugu) {
        this.sugu = sugu;
    }

    public int getSünniaasta() {
        return sünniaasta;
    }

    public void setSünniaasta(int sünniaasta) {
        this.sünniaasta = sünniaasta;
    }

    public String toString(){
        return viit + ", " + sünniaasta + ", " + sugu + ", " + kaal;
    }

}
