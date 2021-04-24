import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.JSONRepresentationOfStructuredReportObjectFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import Model.*;


public class StructuredReportFailiLugeja {

    public static void loeStructuredReportFailist(Uuring uuring, File fail) throws IOException, DicomException {
        AttributeList attributeList = new AttributeList();
        attributeList.read(fail);
        JsonArray jsonArray = new JSONRepresentationOfStructuredReportObjectFactory().getDocument(fail);

//        JSONRepresentationOfStructuredReportObjectFactory.write(new File("testSR_CT.json"),jsonArray);
//        JsonObject sisu = jsonArray.get(0).asJsonObject();
////        System.out.println(sisu.getJsonArray("XRayRadiationDoseReport")
////                .get(1)
////                .asJsonArray()
////                .get(2));
//        JsonParser jsonParser = Json.createParser(new StringReader(jsonArray.get(0).toString()));
//        List<JsonArray> listOfEvents = loeJsonArray(jsonParser,"IrradiationEventXRayData");
//        System.out.println(listOfEvents.get(0));
//        System.out.println(listOfEvents.get(1));
    }

    private static List<JsonArray> loeJsonArray(JsonParser jsonParser, String key){
        List<JsonArray> result = new ArrayList<>();
        while (jsonParser.hasNext()) {
            Event e = jsonParser.next();
            if (e == Event.KEY_NAME) {
                if (jsonParser.getString().equals(key)) {
                    jsonParser.next();
                    result.add(jsonParser.getArray());
                }
            }
        }
        return result;
    }

    private static String loeJsonString(JsonParser jsonParser, String key){
        String result = null;
        while (jsonParser.hasNext()) {
            Event e = jsonParser.next();
            if (e == Event.KEY_NAME) {
                if (jsonParser.getString().equals(key)) {
                    jsonParser.next();
                    result = jsonParser.getString();
                    break;
                }
            }
        }
        return result;
    }


    public static void main(String[] args) throws IOException, DicomException {
        Uuring uuring = new NimmelülidUuring("aa", 88.0);
        loeStructuredReportFailist(uuring, new File("src/test/resources/PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm"));
    }
}
