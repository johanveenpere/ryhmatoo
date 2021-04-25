package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import java.time.LocalDate;
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
    public Map<String, AttributeTag> getEriAtribuudid() {
        return new HashMap<>(Map.of(
                "DoseAreaProduct", new AttributeTag(0x0018,0x115E)
                //,"DistanceSourceToPatient", TagFromName.DistanceSourceToPatient
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

    public String toString(){
        return super.toString() + ", " + doseAreaProduct + ", " + distanceSourceToPatient;
    }

}