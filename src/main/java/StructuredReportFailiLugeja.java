import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.JSONRepresentationOfStructuredReportObjectFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Model.*;


public class StructuredReportFailiLugeja {
    /**
     * Loeb Structured Report tüüpi failist andmed Uuring isendiväljadele.
     * Structured Report failis sisalduvad uuringu üldandmed ja üldiselt mitme AcquisitionEvent kohta.
     * Õige AcquisitionEvent leitakse Uuring isendi meetodist getAcquisitionKey päritava võtme abil,
     * mida võrreldakse välja "AcquisitionProtocol" väärtusega.
     *
     * @param uuring - Uuring isend mille välju hakatkse meetodiga täitma.
     * @param fail   - Structured Report tüüpi fail.
     * @throws IOException
     * @throws DicomException - failist ei saa teha AttributesList objekti.
     */
    public static void loeStructuredReportFailist(Uuring uuring, File fail) throws IOException, DicomException {
        AttributeList attributeList = new AttributeList();
        attributeList.read(fail);
        JsonArray jsonArray = new JSONRepresentationOfStructuredReportObjectFactory().getDocument(fail);
        JsonParser jsonParser = Json.createParser(new StringReader(jsonArray.get(0).toString()));

        uuring.setLoomisaeg(LocalDateTime.parse(loeJsonString(jsonParser, "StudyDate"), DateTimeFormatter.BASIC_ISO_DATE));
        uuring.setSeade(loeJsonString(jsonParser, "StationName"));
        uuring.setSugu(loeJsonString(jsonParser, "PatientSex"));
        uuring.setVanus(Integer.parseInt(loeJsonString(jsonParser, "PatientAge").substring(0, 3)));

        if (uuring instanceof PeaNatiivUuring) {
            List<JsonArray> seeriad = loeJsonArrayList(jsonParser, "CTAcquisition");
            for (JsonArray seeria : seeriad) {
                JsonParser seeriaParser = Json.createParser(new StringReader(seeria.toString()));
                String protocolValue = loeJsonString(seeriaParser, "AcquisitionProtocol");
                if (protocolValue.equals(((PeaNatiivUuring) uuring).getAcquisitionKey())) {
                    ((PeaNatiivUuring) uuring).setCTDIvol(Double.parseDouble(loeJsonValue(seeriaParser, "MeanCTDIvol")));
                    ((PeaNatiivUuring) uuring).setDLP(Double.parseDouble(loeJsonValue(seeriaParser, "DLP")));
                    break;
                }
            }
        }
//        JSONRepresentationOfStructuredReportObjectFactory.write(new File("testSR_CT.json"),jsonArray);
    }

    /**
     * Loeb JsonParserist kõik etteantud võtmele vastavad JsonArrayd ja paneb need listi.
     * Kasutatakse, et SR failsit välja lugeda kõik AcquisitionEventid.
     *
     * @param jsonParser
     * @param key        - JsonArrayle eelnev võti.
     * @return
     */
    private static List<JsonArray> loeJsonArrayList(JsonParser jsonParser, String key) {
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

    /**
     * Loetakse JsonParserist järgmine etteantud võtmele järgnev väärtus.
     * Kasutatakse SR failist lihtväärtuste välja lugemiseks, mis on failis kujul näiteks:
     * "SeriesDate": "20210128"
     *
     * @param jsonParser
     * @param key
     * @return
     */
    private static String loeJsonString(JsonParser jsonParser, String key) {
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

    /**
     * Loetakse JsonParserist järgmine etteantud võtmele vastav numbriline väärtus.
     * Kasutatakse SR failist  numbriliste väärtuste välja lugemiseks, mis on failis kujul näiteks:
     * "MeanCTDIvol": [
     * {
     * "_units": "mGy"
     * },
     * "4.69"
     * ]
     *
     * @param jsonParser
     * @param key
     * @return
     */
    private static String loeJsonValue(JsonParser jsonParser, String key) {
        String result = null;
        while (jsonParser.hasNext()) {
            Event e = jsonParser.next();
            if (e == Event.KEY_NAME) {
                if (jsonParser.getString().equals(key)) {
                    jsonParser.next();
                    result = jsonParser.getArray().getString(1);
                    break;
                }
            }
        }
        return result;
    }
}
