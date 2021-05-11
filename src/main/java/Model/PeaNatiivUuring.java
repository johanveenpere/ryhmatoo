package Model;

import Service.Kriteerium;
import com.pixelmed.dicom.AttributeTag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;

@Entity
public class PeaNatiivUuring extends Uuring {
    @Column
    private double compTomoDoseIndex; //Computed Tomography Dose Index
    @Column
    private double doseLengthProduct; // Dose-length Product

    public PeaNatiivUuring() {
    }

    ;

    public PeaNatiivUuring(String viit, double kaal) {
        super(viit, kaal);
        super.setUuringunimetus("Pea KT natiivis");
        super.setKriteerium(new Kriteerium(85,65,75,0.2,10, LocalDate.of(2020,4,1)));
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

    @Override
    public String toCSVStringVäljadeNimed() {
        return super.toCSVStringVäljadeNimed() + ", " +
                "CTDIvol, " +
                "DLP";

    }

    @Override
    public String toCSVString() {
        return super.toCSVString() + ", "
                + compTomoDoseIndex + ", "
                + doseLengthProduct;
    }
}
