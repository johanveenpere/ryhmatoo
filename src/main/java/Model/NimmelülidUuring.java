package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity
public class NimmelülidUuring extends Uuring {
    @Column
    private double doseAreaProductAP;
    @Column
    private double doseAreaProductLL;
    @Column
    private double distanceSourceToPatientAP;
    @Column
    private double distanceSourceToPatientLL;

    public NimmelülidUuring(String viit, double kaal) {
        super(viit, kaal);
    }

    public NimmelülidUuring() {}

    public double getDoseAreaProductAP() {
        return doseAreaProductAP;
    }

    public double getDistanceSourceToPatientAP() {
        return distanceSourceToPatientAP;
    }

    public double getDoseAreaProductLL() {
        return doseAreaProductLL;
    }

    public double getDistanceSourceToPatientLL() {
        return distanceSourceToPatientLL;
    }

    public void setDoseAreaProductAP(double doseAreaProduct) {
        this.doseAreaProductAP = doseAreaProduct;
    }

    public void setDistanceSourceToPatientAP(double distanceSourceToPatientAP) {
        this.distanceSourceToPatientAP = distanceSourceToPatientAP;
    }

    public void setDoseAreaProductLL(double doseAreaProductLL) {
        this.doseAreaProductLL = doseAreaProductLL;
    }

    public void setDistanceSourceToPatientLL(double distanceSourceToPatientLL) {
        this.distanceSourceToPatientLL = distanceSourceToPatientLL;
    }

    public void setDoseAreaProductAP(String doseAreaProductAP) {
        this.doseAreaProductAP = Double.parseDouble(doseAreaProductAP);
    }

    public void setDistanceSourceToPatientAP(String distanceSourceToPatientAP) {
        this.distanceSourceToPatientAP = Double.parseDouble(distanceSourceToPatientAP);
    }

    public void setDoseAreaProductLL(String doseAreaProductLL) {
        this.doseAreaProductLL = Double.parseDouble(doseAreaProductLL);
    }

    public void setDistanceSourceToPatientLL(String distanceSourceToPatientLL) {
        this.distanceSourceToPatientLL = Double.parseDouble(distanceSourceToPatientLL);
    }

    @Override
    public Map<String, String> getAtribuudid() {
        return new HashMap<>(Map.of(
                "Key", "00185101",
                "Sugu", "00100040",
                "Vanus", "00101010",
                "DoseAreaProduct", "0018115e",
                "DistanceSourceToPatient", "00181110"
        ));
    }

    public String toString() {
        return super.toString() + ", " + doseAreaProductAP + ", " + distanceSourceToPatientAP + ", " + doseAreaProductLL + ", " + distanceSourceToPatientLL;
    }

    @Override
    public void setVanus(String vanus) {
        super.setVanus(vanus);
    }

    @Override
    public void setSugu(String sugu) {
        super.setSugu(sugu);
    }
}
