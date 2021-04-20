import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class KokkuvõtteKoostaja {

    public void teeCSV(List<Uuring> uuringud) throws IOException {
        File csvOutputFile = new File("test.csv");
        csvOutputFile.createNewFile();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            uuringud.forEach(pw::println);
        }
    }
}
