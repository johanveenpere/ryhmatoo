import com.pixelmed.dicom.DicomException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import Model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KujutiseFailiLugejaTest {

    @Test
    public void loebMitmestFailistÕigedVäärtused() throws NoSuchMethodException, DicomException, IOException, IllegalAccessException, InvocationTargetException {
        NimmelülidUuring nimmelülidUuring = new NimmelülidUuring("HTYKRG12103221BB",80.4);
        List<File> failid = Arrays.asList(
                new File("src/test/resources/PATSIENT_TEST.CR.Nimmelülid_AP_L.2.1.2021.03.24.13.31.54.108.34909543.dcm"),
                new File("src/test/resources/PATSIENT_TEST.CR.Nimmelülid_AP_L.1.1.2021.03.24.13.31.54.108.34909532.dcm")
                );

        KujutiseFailiLugeja kujutiseFailiLugeja = new KujutiseFailiLugeja(nimmelülidUuring,failid);
        kujutiseFailiLugeja.loeKujutiseFailist();

        assertEquals("TUKRAX1",nimmelülidUuring.getSeade());
        assertEquals("M",nimmelülidUuring.getSugu());
        assertEquals(31,nimmelülidUuring.getVanus());
        assertEquals(0.213,nimmelülidUuring.getDoseAreaProductAP());
        assertEquals(0.256,nimmelülidUuring.getDoseAreaProductLL());
    }

    @Test
    public void loebÜhestFailistÕigedVäärtused() throws NoSuchMethodException, DicomException, IOException, IllegalAccessException, InvocationTargetException {
        RindkereUuring rindkereUuring = new RindkereUuring("HTYKRG12103221BB",80.4);
        List<File> failid = Arrays.asList(
                new File("src/test/resources/PATSIENT_TEST.CR.Rindkere_PA_sei.1.1.2021.03.24.13.32.32.425.71130489.dcm")
        );

        KujutiseFailiLugeja kujutiseFailiLugeja = new KujutiseFailiLugeja(rindkereUuring,failid);
        kujutiseFailiLugeja.loeKujutiseFailist();

        assertEquals("TUKRAX1",rindkereUuring.getSeade());
        assertEquals("O",rindkereUuring.getSugu());
        assertEquals(31,rindkereUuring.getVanus());
        assertEquals(0.248,rindkereUuring.getDoseAreaProduct());
    }
}
