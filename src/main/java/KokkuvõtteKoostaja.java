import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class KokkuvõtteKoostaja {

    public void teeCSV(List<Uuring> uuringud) throws IOException {
        uuringud.sort(Comparator.comparing(u -> u.getClass().getName()));

        File csvOutputFile = new File("kokkuvõte.csv");
        csvOutputFile.createNewFile();

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            String klass = uuringud.get(0).getClass().getSimpleName();
            String klasstemp;
            pw.println(uuringud.get(0).toStringVäljadeNimed());
            for (Uuring uuring : uuringud) {
                klasstemp = uuring.getClass().getSimpleName();
                if (!klass.equals(klasstemp)) {
                    pw.println("");
                    pw.println(uuring.toStringVäljadeNimed());
                    klass = klasstemp;
                }
                pw.println(uuring.toString());
            }
        }
    }
}
