import java.io.*;

import Model.Uuring;
import Repository.TühiUuringulistException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class KokkuvõtteKoostaja {

    public static void teeCSV(List<Uuring> uuringud, File csvOutputFile) throws IOException {

        if (uuringud.size() == 0) {
            throw new TühiUuringulistException();
        }

        uuringud.sort(Comparator.comparing(u -> u.getClass().getName()));

        csvOutputFile.createNewFile();

        try (OutputStream os = new FileOutputStream(csvOutputFile);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            String klass = uuringud.get(0).getClass().getSimpleName();
            String klasstemp;
            pw.println(klass);
            pw.println(uuringud.get(0).toCSVStringVäljadeNimed());
            for (Uuring uuring : uuringud) {
                klasstemp = uuring.getClass().getSimpleName();
                if (!klass.equals(klasstemp)) {
                    pw.println(klasstemp);
                    pw.println(uuring.toCSVStringVäljadeNimed());
                    klass = klasstemp;
                }
                pw.println(uuring.toCSVString());
            }
        }
    }
}
