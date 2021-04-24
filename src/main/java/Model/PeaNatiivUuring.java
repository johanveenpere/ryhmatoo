package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
public class PeaNatiivUuring extends Uuring {
    @Column
    private double compTomoDoseIndex; //Computed Tomography Dose Index
    @Column
    private double doseLengthProduct; // Dose-length Product
    @Transient
    private String protocolKey;

    public PeaNatiivUuring() {};

    public PeaNatiivUuring(String viit, double kaal) {
        super(viit, kaal);
        protocolKey = "Rinnalylid";
    }

    public double getCTDIvol() {
        return compTomoDoseIndex;
    }

    public double getDLP() {
        return doseLengthProduct;
    }

    public void setCTDIvol(float cTDIvol) {
        this.compTomoDoseIndex = cTDIvol;
    }

    public void setDLP(float dLP) {
        this.doseLengthProduct = dLP;
    }

    public String getProtocolKey() {
        return protocolKey;
    }

    public void setProtocolKey(String protocolKey) {
        this.protocolKey = protocolKey;
    }

    public Map<String, String> getAtribuudid() {
        return null;
    }

    public String toString(){
        return super.toString() + ", " + compTomoDoseIndex + ", " + doseLengthProduct;
    }
}
