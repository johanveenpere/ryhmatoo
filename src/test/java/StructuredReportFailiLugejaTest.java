import Model.*;
import com.pixelmed.dicom.DicomException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StructuredReportFailiLugejaTest {

    @Test
    public void loebSRFailistÕigedVäärtused() throws IOException, DicomException {
        PeaNatiivUuring uuring = new PeaNatiivUuring("HTYKKT303033557T", 88.0);
        File fail = new File("src/test/resources/PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm");
        StructuredReportFailiLugeja.loeStructuredReportFailist(uuring,fail);

        assertEquals("tukforce1",uuring.getSeade());
        assertEquals("O",uuring.getSugu());
        assertEquals(31,uuring.getVanus());
        assertEquals(4.69, uuring.getCTDIvol());
        assertEquals(78.73,uuring.getDLP());
    }
}
