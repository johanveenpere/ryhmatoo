import java.util.Map;

public abstract class Uuring {
    private final String viit;
    private final double kaal;
    private String sugu;
    private int vanus;

    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Uuring(String viit, double kaal) {
        this.viit = viit;
        this.kaal = kaal;
    }

    /**
     * Getterid on kõigile isendiväljadele.
     * Setterid on vajalikud ainult väljadele, mida muudetakse. Viita ja kaalu ei ole vaja ja ei tohiks saadagi hiljem enam muuta (final).
     */

    public String getViit() {
        return viit;
    }

    public double getKaal() {
        return kaal;
    }

    public String getSugu() {
        return sugu;
    }

    public void setSugu(String sugu) {
        this.sugu = sugu;
    }

    public int getVanus() {
        return vanus;
    }

    public void setVanus(int vanus) {
        this.vanus = vanus;
    }

    public void setVanus(String vanus) {
        this.vanus = Integer.parseInt(vanus.substring(0,3));
    }

    public abstract Map<String, String> getAtribuudid();

    public String toString(){
        return this.viit + ", " + this.kaal  + ", " + this.vanus + ", " + this.sugu;
    }

    public String toStringVäljadeNimed() {return "viit, kaal, vanus, sugu";}

}
