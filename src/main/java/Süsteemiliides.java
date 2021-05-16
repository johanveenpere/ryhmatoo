import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import com.pixelmed.dicom.DicomException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Süsteemiliides {

    private EntityManagerFactory emf;
    private EntityManager em;
    private UuringRepository db;

    public Süsteemiliides(String andmebaasiNimi) {
        emf = Persistence.createEntityManagerFactory(andmebaasiNimi);
        em = emf.createEntityManager();
        db = new UuringRepository(em);
    }

    public Süsteemiliides(EntityManagerFactory emf, EntityManager em) {
        db = new UuringRepository(em);
    }

    public Süsteemiliides(UuringRepository db) {
        this.db = db;
    }

    public <T extends Uuring> void UusUuring(String pildiviit, double kaal, Class<T> tüüp) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        //salvestada andmebaasi pildiviit, kaal
        Constructor<T> con = tüüp.getConstructor(String.class, double.class);
        T uuring = con.newInstance(pildiviit, kaal);
        db.addUuring(uuring);
    }

    public void TäidaPuuduvadUuringud(String tempPildikaust) {
        List<Uuring> uuringud = new ArrayList<>();//db.getPoolikudUuringud();
        CGetUuringud serveriühendus = new CGetUuringud();
        for (Uuring uuring : uuringud) {
            String ülemkaust = serveriühendus.TõmbaUuringud(uuring.getViit(), tempPildikaust);
            File path = new File(ülemkaust);
            List<File> failid = Arrays.asList(path.listFiles());
            try {
                KujutiseFailiLugeja kujutiseFailiLugeja = new KujutiseFailiLugeja(uuring,failid);
                kujutiseFailiLugeja.loeKujutiseFailist();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (DicomException e) {
                e.printStackTrace();
            }
        }
    }

    public void teeKokkuvõte(File fail, Class<? extends Uuring> tüüp) throws IOException {
        List<Uuring> uuringud = db.getAllUuringud(tüüp);
        KokkuvõtteKoostaja.teeCSV(uuringud, fail);
    }

    public UuringRepository getDb() {
        return db;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
