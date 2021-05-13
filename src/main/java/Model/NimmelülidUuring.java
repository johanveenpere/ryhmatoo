package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import Service.Kriteerium;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    public Map<String, AttributeTag> getEriAtribuudid() {
        return new HashMap<>(Map.of(
                "Key", TagFromName.ViewPosition,
                "DoseAreaProduct", new AttributeTag(0x0018,0x115E)
                //, "DistanceSourceToPatient", TagFromName.DistanceSourceToPatient
        ));
    }

    @Override
    public String toCSVStringVäljadeNimed() {
        return super.toCSVStringVäljadeNimed() + ", " +
                "DAP_AP, " +
                "DSP_AP, " +
                "DAP_LL, " +
                "DSP_LL";

    }

    @Override
    public String toCSVString() {
        return super.toCSVString() + ", "
                + doseAreaProductAP + ", "
                + distanceSourceToPatientAP + ", "
                + doseAreaProductLL + ", "
                + distanceSourceToPatientLL;
    }

}
