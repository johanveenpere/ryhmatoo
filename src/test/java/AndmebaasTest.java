import javax.persistence.*;

import Model.NimmelülidUuring;
import Model.PeaNatiivUuring;
import Model.RindkereUuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import Service.PuudulikValimException;
import Service.Valim;
import Service.ValimiSelekteerija;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.lang.Math.round;

public class AndmebaasTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        try {
            populateTables(400, emf);
            OffsetDateTime test = OffsetDateTime.of(2020,1,1,1,1,1,1, ZoneOffset.UTC);
            Kriteerium kriteerium = new Kriteerium(80,60,65,1,20, test);
            ValimiSelekteerija<PeaNatiivUuring> selekteerija = new ValimiSelekteerija<>(PeaNatiivUuring.class, emf, kriteerium);
            Valim valim = selekteerija.getValim();
            System.out.println(valim.toString());
        }
        catch (PuudulikValimException e) {
            System.out.println(e.getMessage());
            if (e.getMessage() == PuudulikValimException.exceptionTypes.SOBIMATU_KAALUKESKMINE.toString()) {
                System.out.println(e.getValim().toString());
            }
            else if (e.getMessage() == PuudulikValimException.exceptionTypes.UURINGUTE_MIINIMUM_TÄITMATA.toString()) {
                System.out.println(e.getValim().toString());
            }
        }
        finally {
            emf.close();
        }
    }
    private static String genRandomString(int len) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }
    private static double genRandomDouble(int min, int max) {
        return (Math.random() * (max - min)) + min;
    }
    private static String genRandomSugu() {
        int leftLimit = 77; // M
        int rightLimit = 78; // N
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(1)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    private static double genRandomKaal(int min, int max) {
        return round((genRandomDouble(min, max)*10)) / (double) 10;
    }
    private static int genRandomVanus(int min, int max) {
        return (int) genRandomDouble(min,max);
    }
    static void populateTables(int entries, EntityManagerFactory emf) {
        for (int i = 0; i < entries; i++) {
            EntityManager em = emf.createEntityManager();
            try {
                RindkereUuring rindkereUuring = new RindkereUuring(genRandomString(20), genRandomKaal(40,120));
                rindkereUuring.setSugu(genRandomSugu());
                rindkereUuring.setVanus(genRandomVanus(5,99));
                rindkereUuring.setDistanceSourceToPatient(genRandomDouble(0,10000));
                rindkereUuring.setDoseAreaProduct(genRandomDouble(0,500));

                PeaNatiivUuring peaNatiivUuring = new PeaNatiivUuring(genRandomString(20), genRandomKaal(40,120));
                peaNatiivUuring.setSugu(genRandomSugu());
                peaNatiivUuring.setVanus(genRandomVanus(5,99));
                peaNatiivUuring.setDLP((float) genRandomDouble(0,20));
                peaNatiivUuring.setCTDIvol((float) genRandomDouble(0,20));

                NimmelülidUuring nimmelülidUuring = new NimmelülidUuring(genRandomString(20), genRandomKaal(40,120));
                nimmelülidUuring.setSugu(genRandomSugu());
                nimmelülidUuring.setVanus(genRandomVanus(5,99));
                nimmelülidUuring.setDistanceSourceToPatientAP(genRandomDouble(0,200));
                nimmelülidUuring.setDistanceSourceToPatientLL(genRandomDouble(0,200));

                UuringRepository repo = new UuringRepository(emf, em);
                repo.addUuring(rindkereUuring);
                repo.addUuring(peaNatiivUuring);
                repo.addUuring(nimmelülidUuring);
            }
            finally {
                em.close();
            }
        }
    }
    }
