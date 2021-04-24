package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.*;

@Entity
public class RindkereUuring extends Uuring {
    @Column
    private double doseAreaProduct;
    @Column
    private double distanceSourceToPatient;

    public RindkereUuring(){};

    public RindkereUuring(String viit, double kaal) {
        super(viit, kaal);
    }

    @Override
    public Map<String, String> getAtribuudid() {
        return new HashMap<>(Map.of(
                "Sugu", "00100040",
                "Vanus", "00101010",
                "DoseAreaProduct", "0018115e",
                "DistanceSourceToPatient", "00181110"
        ));
    }

    public double getDoseAreaProduct() {
        return doseAreaProduct;
    }

    public double getDistanceSourceToPatient() {
        return distanceSourceToPatient;
    }

    public void setDoseAreaProduct(double doseAreaProduct) {
        this.doseAreaProduct = doseAreaProduct;
    }

    public void setDistanceSourceToPatient(double distanceSourceToPatient) {
        this.distanceSourceToPatient = distanceSourceToPatient;
    }

    public void setDoseAreaProduct(String doseAreaProduct) {
        this.doseAreaProduct = Double.parseDouble(doseAreaProduct);
    }

    public void setDistanceSourceToPatient(String distanceSourceToPatient) {
        this.distanceSourceToPatient = Double.parseDouble(distanceSourceToPatient);
    }

    public String toString(){
        return super.toString() + ", " + doseAreaProduct + ", " + distanceSourceToPatient;
    }

    @Override
    public void setSugu(String sugu) {
        super.setSugu(sugu);
    }

    @Override
    public void setVanus(String vanus) {
        super.setVanus(vanus);
    }
}
