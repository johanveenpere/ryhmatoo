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
    Map<String, AttributeTag> eriAtribuudid;

    public RindkereUuring(){};

    public RindkereUuring(String viit, double kaal) {
        super(viit, kaal);
        eriAtribuudid = new HashMap<>(Map.of(
                "DoseAreaProduct", new AttributeTag(0x0018,0x115E)
                //,"DistanceSourceToPatient", TagFromName.DistanceSourceToPatient
        ));
    }

    @Override
    public Map<String, AttributeTag> getEriAtribuudid() {
        return eriAtribuudid;
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
