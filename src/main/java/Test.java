import com.pixelmed.dicom.StructuredReport;
import com.pixelmed.validate.DicomSRValidator;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomObjects;
import org.dcm4che2.io.DicomInputStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        //DicomInputStream dicom = new DicomInputStream(new FileInputStream("PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm"));
        DicomObject obj = new BasicDicomObject();
        obj.dataset();
        //Serveriühendus ühendus = new HttpsServeriühendus("http://dicomserver.co.uk:81/qido/studies?StudyInstanceUID=4.5.322.3234.5.54.202006031327590");
        //ühendus.SaaMetaAndmed("");
        DicomSRValidator valideerija = new DicomSRValidator();
        valideerija.validate("PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm");
    }
}
