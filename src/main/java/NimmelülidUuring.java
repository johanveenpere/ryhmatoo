import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.TagFromName;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class NimmelülidUuring extends Uuring {
    private double doseAreaProductAP;
    private double doseAreaProductLL;
    private double distanceSourceToPatientAP;
    private double distanceSourceToPatientLL;
    Map<String, AttributeTag> atribuudid;

    public NimmelülidUuring(String viit, double kaal) {
        super(viit, kaal);
        atribuudid = new HashMap<>(Map.of(
                "Key", TagFromName.ViewPosition,
                "DoseAreaProduct", new AttributeTag(0x0018,0x115E)
                //, "DistanceSourceToPatient", TagFromName.DistanceSourceToPatient
        ));
    }

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

    @Override
    public Map<String, AttributeTag> getEriAtribuudid() {
        return atribuudid;
    }

    public String toString() {
        return super.toString() + ", " + doseAreaProductAP + ", " + distanceSourceToPatientAP + ", " + doseAreaProductLL + ", " + distanceSourceToPatientLL;
    }

}
