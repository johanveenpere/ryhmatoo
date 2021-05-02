package Model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@OnDelete(action = OnDeleteAction.CASCADE)
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
    private OffsetDateTime kuupäev;

    public Uuring() {
    }
    /**
     * Konstruktorit kutsutakse ainult läbi alamklassi. Isend luuakse viida ja kaalu sisestamisel tehniku poolt.
     * Ülejäänud isendiväljade täitmine toimub pärast andmete lugemist pildiinfost set-meetodite abil.
     */
    protected Uuring(String viit, double kaal) {
        this.viit = viit;
        this.kaal = kaal;
        this.kuupäev = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }
    protected Uuring(String viit, double kaal, OffsetDateTime kuupäev) {
        this.viit = viit;
        this.kaal = kaal;
        this.kuupäev = kuupäev;
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

    public OffsetDateTime getKuupäev() {
        return kuupäev;
    }

    public void setKuupäev(OffsetDateTime kuupäev) {
        this.kuupäev = kuupäev;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "={viit='" + viit + '\'' +
                ", kaal=" + kaal +
                ", sugu='" + sugu + '\'' +
                ", vanus=" + vanus +
                ", kuupäev=" + kuupäev +
                '}';
    }

    public String simpleString() {
        return "viit='" + viit + '\'' +
                ", kaal=" + kaal +
                ", sugu='" + sugu + '\'' +
                ", vanus=" + vanus +
                ", kuupäev=" + kuupäev;
    }

    @Override
    public int compareTo(Uuring o) {
        return Double.compare(this.kaal, o.kaal);
    }

}
