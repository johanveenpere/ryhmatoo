import Model.NimmelülidUuring;
import Model.PeaNatiivUuring;
import Model.RindkereUuring;
import Model.Uuring;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyData {
    public static List<Uuring> randomUuringud() {
        int viitLen = 15;
        int minKaal = 40;
        int maxKaal = 120;

        NimmelülidUuring nimmelülidUuring = new NimmelülidUuring(randomString(viitLen), randomDouble(minKaal, maxKaal));
        PeaNatiivUuring peaNatiivUuring = new PeaNatiivUuring(randomString(viitLen), randomDouble(minKaal, maxKaal));
        RindkereUuring rindkereUuring = new RindkereUuring(randomString(viitLen), randomDouble(minKaal, maxKaal));

        return fillData(nimmelülidUuring,peaNatiivUuring,rindkereUuring);
    }
    public static List<Uuring> randomUuringud(double kaal) {
        int viitLen = 15;

        NimmelülidUuring nimmelülidUuring = new NimmelülidUuring(randomString(viitLen), kaal);
        PeaNatiivUuring peaNatiivUuring = new PeaNatiivUuring(randomString(viitLen), kaal);
        RindkereUuring rindkereUuring = new RindkereUuring(randomString(viitLen), kaal);

        return fillData(nimmelülidUuring,peaNatiivUuring,rindkereUuring);
    }
    public static List<Uuring> randomUuringud(String[] viidad) {
        int minKaal = 40;
        int maxKaal = 99;

        NimmelülidUuring nimmelülidUuring = new NimmelülidUuring(viidad[0], randomDouble(minKaal, maxKaal));
        PeaNatiivUuring peaNatiivUuring = new PeaNatiivUuring(viidad[1], randomDouble(minKaal, maxKaal));
        RindkereUuring rindkereUuring = new RindkereUuring(viidad[2], randomDouble(minKaal, maxKaal));
        return fillData(nimmelülidUuring,peaNatiivUuring,rindkereUuring);
    }
    public static List<Uuring> fillData(NimmelülidUuring nimmelülidUuring, PeaNatiivUuring peaNatiivUuring, RindkereUuring rindkereUuring) {
        Random random = new Random();
        List<Uuring> uuringud = new ArrayList<>();
        uuringud.add(nimmelülidUuring);
        uuringud.add(peaNatiivUuring);
        uuringud.add(rindkereUuring);

        int minVanus = 6;
        int maxVanus = 99;

        for (Uuring uuring : uuringud) {
            uuring.setSugu(random.ints(77, 79)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(1)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString()
            );
            uuring.setVanus((int) (Math.random() * (maxVanus - minVanus)) + minVanus);
        }
        nimmelülidUuring.setDistanceSourceToPatientLL(randomDouble(1000, 9999));
        nimmelülidUuring.setDistanceSourceToPatientAP(randomDouble(1000, 9999));
        peaNatiivUuring.setCTDIvol((float) randomDouble(1000, 9999));
        peaNatiivUuring.setDLP((float) randomDouble(1000, 9999));
        rindkereUuring.setDoseAreaProduct((float) randomDouble(1000, 9999));
        rindkereUuring.setDistanceSourceToPatient(randomDouble(1000, 9999));

        return uuringud;
    }
    private static String randomString(int len) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }
    private static double randomDouble(int min, int max) {
        return Math.round(((Math.random() * (max- min)) + min)*100) / (double) 100;
    }
}
