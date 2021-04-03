public class RGAndmed extends Uuring{
    private float doseAreaProduct;
    private float distanceSourceToPatient;

    public RGAndmed(String viit, float kaal) {
        super(viit, kaal);
    }

    public float getDoseAreaProduct() {
        return doseAreaProduct;
    }

    public float getDistanceSourceToPatient() {
        return distanceSourceToPatient;
    }

    public void setDoseAreaProduct(float doseAreaProduct) {
        this.doseAreaProduct = doseAreaProduct;
    }

    public void setDistanceSourceToPatient(float distanceSourceToPatient) {
        this.distanceSourceToPatient = distanceSourceToPatient;
    }

    public String toString(){
        return super.toString() + ", " + doseAreaProduct + ", " + distanceSourceToPatient;
    }

}
