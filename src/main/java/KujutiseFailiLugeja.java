import com.pixelmed.dicom.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Klass DICOM kujutisefailidest info välja lugemiseks. Klassi staatilised meetodid on loodud pidades silmas seda,
 * et uue Uuring alamklassi loomisel ei oleks vaja antud klassi muuta.
 */
public class KujutiseFailiLugeja {

    /**
     * Loeb Uuring isendiväljadele väärtused failidest. Loeb vajadusel nii ühest kui mitmest failist.
     * Kasutab parameetriks antud Uuring isendi getAtribuudid() meetodit info saamiseks, mida on vaja failidest lugeda.
     *
     * @param uuring - Uuring isend, kuhu hakatakse väärtuseid lugema.
     * @param failid - List<File> failidega
     * @throws NoSuchMethodException - uuring isendil puudub set-meetod mida väljade täitmisel kutsutakse
     * @throws InvocationTargetException - kutsutud meetodi visatud ja wrapitud erind
     * @throws IllegalAccessException - kutsutud meetodile pole ligipääsuõigust
     * @throws IOException
     * @throws DicomException - atribuutide nimekirja failist lugemise viga
     */
    public static void loeKujutiseFailist(Uuring uuring, List<File> failid) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, DicomException {
        AttributeList põhiAttributeList = new AttributeList();
        põhiAttributeList.read(failid.get(0));
        Map<String, AttributeTag> põhiAtribuudid = new HashMap<>(uuring.getPõhiAtribuudid());
        loeAtribuudid(põhiAtribuudid, uuring, põhiAttributeList);
        for (File fail : failid) {
            AttributeList eriAttributeList = new AttributeList();
            eriAttributeList.read(fail);
            Map<String, AttributeTag> eriAtribuudid = new HashMap<>(uuring.getEriAtribuudid());
            loeAtribuudid(eriAtribuudid,uuring,eriAttributeList);
        }
    }

    private static void loeAtribuudid(Map<String, AttributeTag> atribuudid, Uuring uuring, AttributeList atribuutList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String key = "";
        if (atribuudid.containsKey(("Key"))) {
            key = atribuutList.get(atribuudid.get("Key")).getSingleStringValueOrEmptyString();
            atribuudid.remove("Key");
        }
        for (Map.Entry<String, AttributeTag> atribuut : atribuudid.entrySet()) {
            Class klass;
            String atribuudiVäärtuseTüüp = atribuutList.get(atribuut.getValue()).getVRAsString();
            Object atribuudiVäärtus;
            switch (atribuudiVäärtuseTüüp) {
                case "AS" -> { // Age String - vanuse edastamiseks kasutatav erivorm kujul "030Y"
                    klass = int.class;
                    atribuudiVäärtus = Integer.parseInt(atribuutList.get(atribuut.getValue()).getSingleStringValueOrDefault("000Y").substring(0, 3));
                }
                case "DS" -> { // Decimal String
                    klass = double.class;
                    atribuudiVäärtus = Double.parseDouble(atribuutList.get(atribuut.getValue()).getSingleStringValueOrDefault("0.0"));
                }
                case "DA" -> { // Date
                    klass = LocalDate.class;
                    atribuudiVäärtus = LocalDate.parse(atribuutList.get(atribuut.getValue()).getSingleStringValueOrDefault("19000101"), DateTimeFormatter.BASIC_ISO_DATE);
                }
                default -> {
                    klass = String.class;
                    atribuudiVäärtus = atribuutList.get(atribuut.getValue()).getSingleStringValueOrEmptyString();
                }
            }
            Method kutsutavMeetod = uuring.getClass().getMethod("set" + atribuut.getKey() + key, klass);
            kutsutavMeetod.invoke(uuring, atribuudiVäärtus);
        }
    }
}
