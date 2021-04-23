package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Map;

@Entity
public class PeaNatiivUuring extends Uuring {
    @Column
    private float compTomoDoseIndex; //Computed Tomography Dose Index
    @Column
    private float doseLengthProduct; // Dose-length Product

    public PeaNatiivUuring() {};

    public PeaNatiivUuring(String viit, double kaal) {
        super(viit, kaal);
    }

    @Override
    public Map<String, String> getAtribuudid() {
        return null;
    }

    public float getCTDIvol() {
        return compTomoDoseIndex;
    }

    public float getDLP() {
        return doseLengthProduct;
    }

    public void setCTDIvol(float cTDIvol) {
        this.compTomoDoseIndex = cTDIvol;
    }

    public void setDLP(float dLP) {
        this.doseLengthProduct = dLP;
    }

    public String toString(){
        return super.toString() + ", " + compTomoDoseIndex + ", " + doseLengthProduct;
    }
}
