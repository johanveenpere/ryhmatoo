import java.util.Map;

public class PeaNatiivUuring extends Uuring{
    private float compTomoDoseIndex; //Computed Tomography Dose Index
    private float doseLengthProduct; // Dose-length Product

    public PeaNatiivUuring(String viit, float kaal) {
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
