public class KTAndmed extends Uuring{
    private float compTomoDoseIndex; //Computed Tomography Dose Index
    private float doseLengthProduct; // Dose-length Product

    public KTAndmed(String viit, float kaal) {
        super(viit, kaal);
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
