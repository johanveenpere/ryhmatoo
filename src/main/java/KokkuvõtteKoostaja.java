import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class KokkuvõtteKoostaja {

    public static void teeCSV(List<Uuring> uuringud, String failinimi) throws IOException {
        uuringud.sort(Comparator.comparing(u -> u.getClass().getName()));

        File csvOutputFile = new File(failinimi);
        csvOutputFile.createNewFile();

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            String klass = uuringud.get(0).getClass().getSimpleName();
            String klasstemp;
            pw.println(klass);
            pw.println(uuringud.get(0).toStringVäljadeNimed());
            for (Uuring uuring : uuringud) {
                klasstemp = uuring.getClass().getSimpleName();
                if (!klass.equals(klasstemp)) {
                    pw.println(klasstemp);
                    pw.println(uuring.toStringVäljadeNimed());
                    klass = klasstemp;
                }
                pw.println(uuring.toString());
            }
        }
    }
}
