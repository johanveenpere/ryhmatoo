import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public abstract class Uuring {
    private final String viit;
    private final double kaal;
    private String sugu;
    private int vanus;
    private String seade;
    private LocalDate kuupäev;
    private Map<String, AttributeTag> põhiAtribuudid;

    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Uuring(String viit, double kaal) {
        this.viit = viit;
        this.kaal = kaal;
        põhiAtribuudid = new HashMap<>(Map.of(
                "Sugu", TagFromName.PatientSex,
                "Vanus", TagFromName.PatientAge,
                "Seade", TagFromName.StationName,
                "Kuupäev",TagFromName.AcquisitionDate
        ));
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

    public void setSeade(String seade) {
        this.seade = seade;
    }

    public void setKuupäev(LocalDate kuupäev) {
        this.kuupäev = kuupäev;
    }

    public String getSeade() {
        return seade;
    }

    public LocalDate getKuupäev() {
        return kuupäev;
    }

    public Map<String, AttributeTag> getPõhiAtribuudid() {
        return põhiAtribuudid;
    }

    public abstract Map<String, AttributeTag> getEriAtribuudid();

    public String toString(){
        return this.kuupäev + ", " +this.seade + ", " + this.viit + ", " + this.kaal  + ", " + this.vanus + ", " + this.sugu;
    }

}
