import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.JSONRepresentationOfStructuredReportObjectFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.*;
import javax.json.stream.JsonParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class StructuredReportFailiLugeja {

    public static void loeStructuredReportFailist(Uuring uuring, File fail) throws IOException, DicomException {
        AttributeList attributeList = new AttributeList();
        attributeList.read(fail);
        JsonArray jsonArray = new JSONRepresentationOfStructuredReportObjectFactory().getDocument(fail);
        //JSONRepresentationOfStructuredReportObjectFactory.write(new File("testSR.json"),jsonArray);
        JsonObject sisu = jsonArray.get(0).asJsonObject();
//        System.out.println(sisu.getJsonArray("XRayRadiationDoseReport")
//                .get(1)
//                .asJsonArray()
//                .get(2));
        JsonParser jsonParser = Json.createParser(new StringReader(jsonArray.get(0).toString()));
        int count = 0;
        JsonArray result = null;

        while (jsonParser.hasNext()) {
            Event e = jsonParser.next();
            if (e == Event.KEY_NAME) {
                if (jsonParser.getString().equals("IrradiationEventXRayData")) {
                    jsonParser.next();
                    result = jsonParser.getArray();
                    break;
                }
            }
        }
        System.out.println(result);
    }




    public static void main(String[] args) throws IOException, DicomException {
        loeStructuredReportFailist(new NimmelülidUuring("aa", 88.0), new File("src/test/resources/PATSIENT_TEST.SR.Nimmelülid_AP_L.0.1.2021.03.24.13.28.58.233.66260287.dcm"));
    }
}
