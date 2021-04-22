import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelmed.dicom.*;

import javax.json.JsonArray;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import Model.Uuring;
import java.util.*;

/**
 * Klass DICOM kujutisefailidest info välja lugemiseks. Klassi staatilised meetodid on loodud pidades silmas seda,
 * et uue Uuring alamklassi loomisel ei oleks vaja antud klassi muuta.
 */
public class KujutiseFailiLugeja {

    /**
     * Loeb Uuring isendiväljadele väärtused failidest. Loeb vajadusel nii ühest kui mitmest failist. Kasutab parameetriks antud Uuring isendi getAtribuudid() meetodit info saamiseks, mida on vaja failidest lugeda.
     *
     * @param uuring - Uuring isend, kuhu hakatakse väärtuseid lugema.
     * @param failid - List<File> failidega
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws DicomException
     */
    public static void loeKujutiseFailist(Uuring uuring, List<File> failid) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, DicomException {
        for (File fail : failid) {
            Map<String, String> atribuudid = new HashMap<>(uuring.getAtribuudid());
            JsonNode jsonNode = teeJsonNode(fail);
            String key = "";
            if (atribuudid.containsKey(("Key"))) {
                key = loeVäärtus(jsonNode, atribuudid.get("Key"));
                atribuudid.remove("Key");
            }
            for (Map.Entry<String, String> atribuut : atribuudid.entrySet()) {
                String atribuudiVäärtus = loeVäärtus(jsonNode, atribuut.getValue());
                Method kutsutavMeetod;
                if (atribuut.getKey().equals("Vanus") || atribuut.getKey().equals("Sugu"))
                    kutsutavMeetod = uuring.getClass().getDeclaredMethod("set" + atribuut.getKey(), String.class);
                else
                    kutsutavMeetod = uuring.getClass().getDeclaredMethod("set" + atribuut.getKey() + key, String.class);
                kutsutavMeetod.invoke(uuring, atribuudiVäärtus);
            }
        }
    }

    /**
     * Privaatne meetod väärtuse lugemiseks atribuudi koodile vastavalt Value node'ist.
     * Atribuudile vastava välja kuju JSONisse serialiseerituna:
     * "00181110": {
     * "vr": "DS",
     * "Value": [
     * "1804"
     * ]
     */
    private static String loeVäärtus(JsonNode jsonNode, String atribuut) {
        String väärtus;
        try {
            väärtus = jsonNode
                    .get(atribuut)
                    .get("Value")
                    .get(0)
                    .asText();
        } catch (NullPointerException e) {
            return "";
        }
        return väärtus;
    }

    private static JsonNode teeJsonNode(File fail) throws IOException, DicomException {
        AttributeList attributeList = new AttributeList();
            /* AttributeList.read(...)
              Parameters:
              name - the input file name
              transferSyntaxUID - the transfer syntax to use for the data set (leave null for autodetection)
              hasMeta - look for a meta information header
              useBufferedStream - buffer the input for better performance
             */
        attributeList.read(fail);
        JsonArray jsonArray = new JSONRepresentationOfDicomObjectFactory().getDocument(attributeList);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonArray.get(0).toString());
        return jsonNode;
    }

}
