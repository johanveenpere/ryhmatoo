import com.pixelmed.dicom.DicomException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RindkereUuring extends Uuring{
    private float doseAreaProduct;
    private float distanceSourceToPatient;

    /**
     * Meetod mis täidab isendiväljad väärtustega, pärides need etteantud failist KujutiseFailiLugeja klassi staatilise meetodi abil.
     * Failist lugemise meetodile antakse edasi failinimi ja listina DICOM atribuutide koodid, mida on vaja välja lugeda.
     * Tagasi saadakse  nimekiri atribuutidele vastavatest väärtustest.
     * @param failinimi - antud juhul on vaja lugeda andmed mitmest failist, seetõttu on sisendis List failinimedega
     */
    public void laeAndmed(String failinimi) throws IOException, DicomException {
        List<String> atribuutideNimekiri = new ArrayList<>(Arrays.asList(
                "00100040", //sugu
                "00101010", //vanus
                "0018115e", //doseAreaProduct
                "00181110"  //distanceSourceToPatient
        ));
        List<String> andmeteNimekiri = KujutiseFailiLugeja.loeKujutiseFailist(failinimi, atribuutideNimekiri);
                setSugu(andmeteNimekiri.get(0));
                setVanus(Integer.parseInt(andmeteNimekiri.get(1).substring(0,2))); //vanus on dicomis kujul nt "031Y"
                doseAreaProduct = Float.parseFloat(andmeteNimekiri.get(2));
                distanceSourceToPatient = Float.parseFloat(andmeteNimekiri.get(3));
    }

    public RindkereUuring(String viit, float kaal) {
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
