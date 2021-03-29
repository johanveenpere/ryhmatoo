import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomFileUtilities;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.dicom.SetOfDicomFiles;

import java.io.File;
import java.io.IOException;

public class NimmelülidUuring extends Uuring{
    private float doseAreaProductAP;
    private float doseAreaProductLAT;
    private float distanceSourceToPatientAP;
    private float distanceSourceToPatientLAT;


    public NimmelülidUuring(String viit, float kaal) {
        super(viit, kaal);
    }

    public float getDoseAreaProductAP() {
        return doseAreaProductAP;
    }

    public float getDistanceSourceToPatientAP() {
        return distanceSourceToPatientAP;
    }

    public void setDoseAreaProductAP(float doseAreaProduct) {
        this.doseAreaProductAP = doseAreaProduct;
    }

    public void setDistanceSourceToPatientAP(float distanceSourceToPatientAP) {
        this.distanceSourceToPatientAP = distanceSourceToPatientAP;
    }

    public float getDoseAreaProductLAT() {
        return doseAreaProductLAT;
    }

    public float getDistanceSourceToPatientLAT() {
        return distanceSourceToPatientLAT;
    }

    public void setDoseAreaProductLAT(float doseAreaProductLAT) {
        this.doseAreaProductLAT = doseAreaProductLAT;
    }

    public void setDistanceSourceToPatienLAT(float distanceSourceToPatientLAT) {
        this.distanceSourceToPatientLAT = distanceSourceToPatientLAT;
    }

    public String toString(){
        return super.toString() + ", " + doseAreaProductAP + ", " + distanceSourceToPatientAP+ ", " + doseAreaProductLAT + ", " + distanceSourceToPatientLAT;
    }


}
