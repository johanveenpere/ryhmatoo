package Model;

import javax.persistence.Column;
import javax.persistence.Entity;

import Service.Kriteerium;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
        super.setUuringunimetus("Rindkere PA");
        super.setKriteerium(new Kriteerium(85,65,75,0.2,10, LocalDate.of(2020,4,1)));
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

    @Override
    public String toCSVStringVäljadeNimed() {
        return super.toCSVStringVäljadeNimed() + ", " +
                "DAP, " +
                "DSP";

    }

    @Override
    public String toCSVString(){
        return super.toCSVString() + ", "
                + doseAreaProduct + ", "
                + distanceSourceToPatient;
    }

}
