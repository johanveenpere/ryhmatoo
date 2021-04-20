import java.util.*;

public class RindkereUuring extends Uuring{
    private double doseAreaProduct;
    private double distanceSourceToPatient;
    Map<String, String> atribuudid;

    public RindkereUuring(String viit, double kaal) {
        super(viit, kaal);
        atribuudid = new HashMap<>(Map.of(
                "Sugu", "00100040",
                "Vanus", "00101010",
                "DoseAreaProduct", "0018115e",
                "DistanceSourceToPatient", "00181110"
        ));
    }

    @Override
    public Map<String, String> getAtribuudid() {
        return atribuudid;
    }

    public double getDoseAreaProduct() {
        return doseAreaProduct;
    }

    public double getDistanceSourceToPatient() {
        return distanceSourceToPatient;
    }

    public void setDoseAreaProduct(double doseAreaProduct) {
        this.doseAreaProduct = doseAreaProduct;
    }

    public void setDistanceSourceToPatient(double distanceSourceToPatient) {
        this.distanceSourceToPatient = distanceSourceToPatient;
    }

    public void setDoseAreaProduct(String doseAreaProduct) {
        this.doseAreaProduct = Double.parseDouble(doseAreaProduct);
    }

    public void setDistanceSourceToPatient(String distanceSourceToPatient) {
        this.distanceSourceToPatient = Double.parseDouble(distanceSourceToPatient);
    }

    public String toString(){
        return super.toString() + ", " + doseAreaProduct + ", " + distanceSourceToPatient;
    }

    @Override
    public void setSugu(String sugu) {
        super.setSugu(sugu);
    }

    @Override
    public void setVanus(String vanus) {
        super.setVanus(vanus);
    }
}
