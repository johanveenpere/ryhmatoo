import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomObjects;
import org.dcm4che2.io.DicomInputStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //DicomInputStream dicom = new DicomInputStream(new FileInputStream("PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm"));
        DicomObject obj = new BasicDicomObject();
        obj.dataset();
    }
}
