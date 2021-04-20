import com.pixelmed.dicom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NimmelülidUuring extends Uuring {
    private float doseAreaProductAP;
    private float doseAreaProductLAT;
    private float distanceSourceToPatientAP;
    private float distanceSourceToPatientLAT;

    /**
     * Meetod mis täidab isendiväljad väärtustega, pärides need etteantud failist KujutiseFailiLugeja klassi staatilise meetodi abil.
     * Failist lugemise meetodile antakse edasi listidena failinimed ja DICOM atribuutide koodid, mida on vaja välja lugeda.
     * Tagasi saadakse nimekiri, kus on iga faili kohta omakorda nimekiri atribuutidele vastavatest väärtustest.
     * Isendiväljade täitmiseks on vaja teada, millisest failist pärinevaid andmeid parajasti loetakse,
     * selle jaoks on loodud nn võtmeatribuut, mille abil neil vahet tehakse.
     * @param failinimed - antud juhul on vaja lugeda andmed mitmest failist, seetõttu on sisendis List failinimedega
     */
    public void laeAndmed(List<String> failinimed) throws IOException, DicomException {
        List<String> atribuutideNimekiri = new ArrayList<>(Arrays.asList(
                "00185105", //vaade (AP või LAT) - võtmeatribuut
                "00100040", //sugu
                "00101010", //vanus
                "0018115e", //doseAreaProduct
                "00181110"  //distanceSourceToPatient
        ));
        List<List<String>> andmeteNimekirjad = KujutiseFailiLugeja.loeKujutiseFailist(failinimed, atribuutideNimekiri);
        for (List<String> andmeteNimekiri : andmeteNimekirjad
        ) {
            switch (andmeteNimekiri.get(0)) {
                case "AP" -> {
                    setSugu(andmeteNimekiri.get(1));
                    setVanus(Integer.parseInt(andmeteNimekiri.get(2).substring(0, 2))); //vanus on dicomis kujul nt "031Y"
                    doseAreaProductAP = Float.parseFloat(andmeteNimekiri.get(3));
                    distanceSourceToPatientAP = Float.parseFloat(andmeteNimekiri.get(4));
                }
                case "LAT" -> {
                    doseAreaProductLAT = Float.parseFloat(andmeteNimekiri.get(3));
                    distanceSourceToPatientLAT = Float.parseFloat(andmeteNimekiri.get(4));
                }
            }
        }
    }

    public NimmelülidUuring(String viit, float kaal) {
        super(viit, kaal);
    }

    public float getDoseAreaProductAP() {
        return doseAreaProductAP;
    }

    public float getDistanceSourceToPatientAP() {
        return distanceSourceToPatientAP;
    }

    public void setDoseAreaProductAP(float doseAreaProduct) {
        this.doseAreaProductAP = doseAreaProduct;
    }

    public void setDistanceSourceToPatientAP(float distanceSourceToPatientAP) {
        this.distanceSourceToPatientAP = distanceSourceToPatientAP;
    }

    public float getDoseAreaProductLAT() {
        return doseAreaProductLAT;
    }

    public float getDistanceSourceToPatientLAT() {
        return distanceSourceToPatientLAT;
    }

    public void setDoseAreaProductLAT(float doseAreaProductLAT) {
        this.doseAreaProductLAT = doseAreaProductLAT;
    }

    public void setDistanceSourceToPatienLAT(float distanceSourceToPatientLAT) {
        this.distanceSourceToPatientLAT = distanceSourceToPatientLAT;
    }

    public String toString() {
        return super.toString() + ", " + doseAreaProductAP + ", " + distanceSourceToPatientAP + ", " + doseAreaProductLAT + ", " + distanceSourceToPatientLAT;
    }


}
