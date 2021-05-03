import com.pixelmed.dicom.DicomException;
import org.junit.jupiter.api.Test;

import Model.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KokkuvõtteKoostajaTest {

    @Test
    public void koostabKokkuvõtte() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, DicomException {

        int random = ThreadLocalRandom.current().nextInt(10000,99999);
        String failinimi = "testKokkuvõtteFail_"+random+".csv";
        KokkuvõtteKoostaja.teeCSV(testUuringList(),failinimi);

        File testfail = new File(failinimi);
        assertTrue(testfail.exists());
        //testfail.delete();
    }

    private List<Uuring> testUuringList() throws NoSuchMethodException, DicomException, IOException, IllegalAccessException, InvocationTargetException {

        List<File> nimFailid = Arrays.asList(
                new File("src/test/resources/PATSIENT_TEST.CR.Nimmelülid_AP_L.2.1.2021.03.24.13.31.54.108.34909543.dcm"),
                new File("src/test/resources/PATSIENT_TEST.CR.Nimmelülid_AP_L.1.1.2021.03.24.13.31.54.108.34909532.dcm")
        );

        List<File> rinFailid = Arrays.asList(
                new File("src/test/resources/PATSIENT_TEST.CR.Rindkere_PA_sei.1.1.2021.03.24.13.32.32.425.71130489.dcm")
        );

        File ktFail = new File("src/test/resources/PATSIENT_TEST.SR.Spine_RINNALYLI.501.1.2021.03.18.15.36.13.135.23949794.dcm");

        NimmelülidUuring nimmelülidUuring1 = new NimmelülidUuring("AA11", 81.2);
        NimmelülidUuring nimmelülidUuring2 = new NimmelülidUuring("AA12", 81.7);
        NimmelülidUuring nimmelülidUuring3 = new NimmelülidUuring("AA13", 255.7);
        KujutiseFailiLugeja.loeKujutiseFailist(nimmelülidUuring1,nimFailid);
        KujutiseFailiLugeja.loeKujutiseFailist(nimmelülidUuring2,nimFailid);
        KujutiseFailiLugeja.loeKujutiseFailist(nimmelülidUuring3,nimFailid);

        RindkereUuring rindkereUuring1 = new RindkereUuring("BB223", 55.6);
        RindkereUuring rindkereUuring2 = new RindkereUuring("BB203", 66.6);
        KujutiseFailiLugeja.loeKujutiseFailist(rindkereUuring1,rinFailid);
        KujutiseFailiLugeja.loeKujutiseFailist(rindkereUuring2,rinFailid);

        PeaNatiivUuring peaNatiivUuring1 = new PeaNatiivUuring("FF5", 70.5);
        PeaNatiivUuring peaNatiivUuring2 = new PeaNatiivUuring("FF6", 55.5);
        StructuredReportFailiLugeja.loeStructuredReportFailist(peaNatiivUuring1,ktFail);
        StructuredReportFailiLugeja.loeStructuredReportFailist(peaNatiivUuring2,ktFail);

        List<Uuring> uuringud = Arrays.asList(peaNatiivUuring1,nimmelülidUuring1,nimmelülidUuring2,rindkereUuring1,peaNatiivUuring2,rindkereUuring2,nimmelülidUuring3);

        return uuringud;
    }
}
