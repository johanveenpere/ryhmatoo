import java.util.Map;

public class PeaNatiivUuring extends Uuring{
    private double compTomoDoseIndex; //Computed Tomography Dose Index
    private double doseLengthProduct; // Dose-length Product

    public PeaNatiivUuring(String viit, double kaal) {
        super(viit, kaal);
    }

    @Override
    public Map<String, String> getAtribuudid() {
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

    public String toString(){
        return super.toString() + ", " + compTomoDoseIndex + ", " + doseLengthProduct;
    }

    @Override
    public String toStringVäljadeNimed() {
        return super.toStringVäljadeNimed() + ", CTDI, DLP";
    }
}
