package Model;

import Service.Konfiguratsioonid;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UURING")
public abstract class Uuring implements Comparable<Uuring> {
    @Id
    private String viit;
    @Column
    private double kaal;
    @Column
    private String sugu;
    @Column
    private int vanus;
    @Column
    private String seade;
    @Column(columnDefinition = "DATETIME(0)")
    private LocalDateTime loomiseaeg;
    @Column
    private boolean täidetud = false;

    public Uuring() {
    }

    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Uuring(String viit, double kaal) {
        this.viit = viit;
        this.kaal = kaal;
        this.loomiseaeg = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Getterid on kõigile isendiväljadele.
     * Setterid on vajalikud ainult väljadele, mida muudetakse. Viita ja kaalu ei ole vaja ja ei tohiks saadagi hiljem enam muuta (final).
     */

    public boolean isTäidetud() {
        return täidetud;
    }

    public void setTäidetud(boolean täidetud) {
        this.täidetud = täidetud;
    }

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

    public void setLoomiseaeg(LocalDateTime kuupäev) {
        this.loomiseaeg = kuupäev;
    }

    public String getSeade() {
        return seade;
    }

    public LocalDateTime getLoomiseaeg() {
        return loomiseaeg;
    }

    public String getUuringunimetus() {
        Konfiguratsioonid konfiguratsioonid = new Konfiguratsioonid();
        return konfiguratsioonid.getKlassidnimedmap().get(this.getClass());
    }

    public Map<String, AttributeTag> getPõhiAtribuudid() {
        return new HashMap<>(Map.of(
                "Sugu", TagFromName.PatientSex,
                "Vanus", TagFromName.PatientAge,
                "Seade", TagFromName.StationName
        ));
    }

    public abstract Map<String, AttributeTag> getEriAtribuudid();


    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "={viit='" + viit + '\'' +
                ", kaal=" + kaal +
                ", sugu='" + sugu + '\'' +
                ", vanus=" + vanus +
                ", loomiseaeg=" + loomiseaeg +
                '}';
    }

    public String simpleString() {
        return "viit='" + viit + '\'' +
                ", kaal=" + kaal +
                ", sugu='" + sugu + '\'' +
                ", vanus=" + vanus +
                ", loomiseaeg=" + loomiseaeg +
                ", täidetud=" + täidetud;
    }

    public String toCSVStringVäljadeNimed() {
        return "Kuupäev, " +
                "Seade, " +
                "Viit, " +
                "Kaal, " +
                "Vanus, " +
                "Sugu";
    }

    public String toCSVString() {
        return this.loomiseaeg + ", "
                + this.seade + ", "
                + this.viit + ", "
                + this.kaal + ", "
                + this.vanus + ", "
                + this.sugu;
    }

    @Override
    public int compareTo(Uuring o) {
        return o.getLoomiseaeg().compareTo(loomiseaeg);
    }

}
