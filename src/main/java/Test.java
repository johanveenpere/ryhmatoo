import com.pixelmed.dicom.*;
import com.pixelmed.validate.DicomSRValidator;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomObjects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        //DicomInputStream dicom = new DicomInputStream(new FileInputStream("PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm"));
        //DicomObject obj = new BasicDicomObject();
        //obj.dataset();
        //Serveriühendus ühendus = new HttpsServeriühendus("http://dicomserver.co.uk:81/qido/studies?StudyInstanceUID=4.5.322.3234.5.54.202006031327590");
        //ühendus.SaaMetaAndmed("");
        //DicomSRValidator valideerija = new DicomSRValidator();
        //valideerija.validate("PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm");

        DicomInputStream dicom = new DicomInputStream(new File("PATSIENT_TEST.CR.Rindkere_PA_sei.1.1.2021.03.24.13.32.32.425.71130489.dcm"));
        AttributeList attributeList = new AttributeList();
        attributeList.read(dicom);
        AttributeTag tag = new AttributeTag(0x0010,0x0010);
        System.out.println(attributeList.get(tag).getDelimitedStringValuesOrEmptyString());
        dicom.close();


    }
}
