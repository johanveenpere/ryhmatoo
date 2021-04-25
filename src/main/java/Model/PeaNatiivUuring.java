package Model;

import com.pixelmed.dicom.AttributeTag;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Map;

@Entity
public class PeaNatiivUuring extends Uuring {
    @Column
    private double compTomoDoseIndex; //Computed Tomography Dose Index
    @Column
    private double doseLengthProduct; // Dose-length Product

    public PeaNatiivUuring() {};

    public PeaNatiivUuring(String viit, double kaal) {
        super(viit, kaal);
    }

    @Override
    public Map<String, AttributeTag> getEriAtribuudid() {
        return null;
    }

    public double getCTDIvol() {
        return compTomoDoseIndex;
    }

    public double getDLP() {
        return doseLengthProduct;
    }

    public void setCTDIvol(double cTDIvol) {
        this.compTomoDoseIndex = cTDIvol;
    }

    public void setDLP(double dLP) {
        this.doseLengthProduct = dLP;
    }

    public String getAcquisitionKey() {
        return "Rinnalylid";
    }

    public Map<String, String> getAtribuudid() {
        return null;
    }

    public String toString(){
        return super.toString() + ", " + compTomoDoseIndex + ", " + doseLengthProduct;
    }
}
