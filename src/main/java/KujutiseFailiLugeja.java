import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelmed.dicom.*;

import javax.json.JsonArray;
import java.io.IOException;
import java.util.*;

/**
 * Klass DICOM kujutisefailidest info välja lugemiseks. Klassi staatilised meetodid on loodud pidades silmas seda,
 * et uue Uuring alamklassi loomisel ei oleks vaja antud klassi muuta.
 */
public class KujutiseFailiLugeja {

    /**
     * Meetod andmete lugemiseks juhul kui on vaja lugeda mitu sama tüüpi faili.
     * @param atribuutideNimekiri - nimekiri DICOM atribuutidest (kuju: "00010002"), mis tuleb välja lugeda
     * @return - Nimekiri nimekirjadest, kus sisalduvad välja loetud andmed samas järjekorras nagu oli sisendi nimekirjas.
     */
    public static List<List<String>> loeKujutiseFailist(List<String> failinimed,  List<String> atribuutideNimekiri) throws IOException, DicomException {
        List<List<String>> andmeteNimekirjad = new ArrayList<>();
        for (String failinimi:failinimed) {
            List<String> andmed = new ArrayList<>();
            JsonNode jsonNode = teeJsonNode(failinimi);
            for (String atribuut:atribuutideNimekiri) {
                andmed.add(loeVäärtus(jsonNode,atribuut));
            }
            andmeteNimekirjad.add(andmed);
        }
        return andmeteNimekirjad;
    }

    /**
     * Meetod andmete lugemiseks juhul kui on vaja lugeda üks fail.
     * @param atribuutideNimekiri - nimekiri DICOM atribuutidest (kuju: "00010002"), mis tuleb välja lugeda
     * @return - Nimekiri nimekirjadest, kus sisalduvad välja loetud andmed samas järjekorras nagu oli sisendi nimekirjas.
     */
    public static List<String> loeKujutiseFailist(String failinimi,  List<String> atribuutideNimekiri) throws IOException, DicomException {
        List<String> andmed = new ArrayList<>();
        JsonNode jsonNode = teeJsonNode(failinimi);
        for (String atribuut:atribuutideNimekiri) {
            andmed.add(loeVäärtus(jsonNode,atribuut));
        }
        return andmed;
    }

    /**
     * Privaatne meetod väärtuse lugemiseks atribuudi koodile vastavalt Value node'ist.
     * Atribuudile vastava välja kuju JSONisse serialiseerituna:
     * "00181110": {
     *       "vr": "DS",
     *       "Value": [
     *         "1804"
     *       ]
     */
    private static String loeVäärtus(JsonNode jsonNode, String atribuut){
        return jsonNode
                .get(atribuut)
                .get("Value")
                .get(0)
                .asText();
    }

    private static JsonNode teeJsonNode(String failinimi) throws IOException, DicomException {
        AttributeList attributeList = new AttributeList();
            /* AttributeList.read(...)
              Parameters:
              name - the input file name
              transferSyntaxUID - the transfer syntax to use for the data set (leave null for autodetection)
              hasMeta - look for a meta information header
              useBufferedStream - buffer the input for better performance
             */
        attributeList.read(failinimi, null, true, true);
        JsonArray jsonArray = new JSONRepresentationOfDicomObjectFactory().getDocument(attributeList);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonArray.get(0).toString());
        return jsonNode;
    }

}
