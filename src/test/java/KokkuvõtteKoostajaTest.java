import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KokkuvõtteKoostajaTest {

    @Test
    public void koostabKokkuvõtte() throws IOException {
        String failinimi = "testfail.csv";
        KokkuvõtteKoostaja.teeCSV(testUuringList(),failinimi);

        File testfail = new File(failinimi);
        assertTrue(testfail.exists());
    }

    private List<Uuring> testUuringList(){
        NimmelülidUuring nimmelülidUuring1 = new NimmelülidUuring("AA11", 81.2);
        NimmelülidUuring nimmelülidUuring2 = new NimmelülidUuring("AA12", 81.7);
        NimmelülidUuring nimmelülidUuring3 = new NimmelülidUuring("AA13", 255.7);

        RindkereUuring rindkereUuring1 = new RindkereUuring("BB223", 55.6);
        RindkereUuring rindkereUuring2 = new RindkereUuring("BB203", 66.6);

        PeaNatiivUuring peaNatiivUuring1 = new PeaNatiivUuring("FF5", 70.5);
        PeaNatiivUuring peaNatiivUuring2 = new PeaNatiivUuring("FF6", 55.5);


        nimmelülidUuring1.setVanus(55);
        nimmelülidUuring2.setVanus(65);
        nimmelülidUuring3.setVanus(75);

        rindkereUuring1.setVanus(55);
        rindkereUuring2.setVanus(78);

        peaNatiivUuring1.setVanus(67);
        peaNatiivUuring2.setVanus(43);

        nimmelülidUuring1.setSugu("N");
        nimmelülidUuring2.setSugu("M");
        nimmelülidUuring3.setSugu("M");

        rindkereUuring1.setSugu("M");
        rindkereUuring2.setSugu("N");

        peaNatiivUuring1.setSugu("N");
        peaNatiivUuring2.setSugu("M");

        nimmelülidUuring1.setDoseAreaProductAP(0.333);
        nimmelülidUuring2.setDoseAreaProductAP(0.355);
        nimmelülidUuring3.setDoseAreaProductAP(0.123);
        nimmelülidUuring1.setDoseAreaProductLL(0.656);
        nimmelülidUuring2.setDoseAreaProductLL(0.555);
        nimmelülidUuring3.setDoseAreaProductLL(0.155);


        rindkereUuring1.setDoseAreaProduct(0.999);
        rindkereUuring2.setDoseAreaProduct(0.555);

        peaNatiivUuring1.setCTDIvol(434);
        peaNatiivUuring2.setCTDIvol(554);
        peaNatiivUuring1.setDLP(1112.8);
        peaNatiivUuring2.setDLP(4543.5);

        List<Uuring> uuringud = Arrays.asList(peaNatiivUuring1,nimmelülidUuring1,nimmelülidUuring2,rindkereUuring1,peaNatiivUuring2,rindkereUuring2,nimmelülidUuring3);
        return uuringud;
    }
}
