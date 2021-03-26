public class Uuring {
    private String viit;
    private float kaal;
    private String sugu;
    private int vanus;
    private float doosiandmed;
    private String kande_kuupaev;
    private String seade_id;

    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Uuring(String viit, float kaal) {
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

    public int getVanus() {
        return vanus;
    }

    public void setVanus(int vanus) {
        this.vanus = vanus;
    }

    public float getDoosiandmed() {return this.doosiandmed;}

    public void setDoosiandmed(float doosiandmed) {
        this.doosiandmed = doosiandmed;
    }

    public String getKande_kuupaev() {
        return kande_kuupaev;
    }

    public void setKande_kuupaev(String kande_kuupaev) {
        this.kande_kuupaev = kande_kuupaev;
    }

    public String getSeadme_id() {
        return seadme_id;
    }

    public void setSeadme_id(String seadme_id) {
        this.seadme_id = seadme_id;
    }

    private String seadme_id;

    public String getSeade_id() {
        return seade_id;
    }

    public void setSeade_id(String seade_id) {
        this.seade_id = seade_id;
    }

    public String toString(){
        return this.viit + ", " +
                this.kaal  + "kg, " +
                this.sugu + ", " +
                this.doosiandmed + ", " +
                this.vanus + "a, " +
                this.seadme_id + ", " +
                this.kande_kuupaev;
    }

}
