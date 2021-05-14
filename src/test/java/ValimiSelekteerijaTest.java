import Model.NimmelülidUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import Service.PuudulikValimException;
import Service.Valim;
import Service.ValimiSelekteerija;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValimiSelekteerijaTest {
    static EntityManagerFactory emf;
    static EntityManager em;
    static UuringRepository repo;

    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
        repo = new UuringRepository(emf);
    }

    @AfterAll
    public static void destroy() {
        em.close();
        emf.close();
    }

    @BeforeEach
    public void clean() {
        repo.deleteAll();
    }

    @RepeatedTest(5)
    public void töötabMinimaalseValimiga() {
        try {
            List<Uuring> uuringud = DummyData.randomUuringud(80);
            uuringud = Stream.concat(uuringud.stream(), DummyData.randomUuringud(20).stream()).collect(Collectors.toList());
            for (int i = 0; i < 8; i++) {
                uuringud = Stream.concat(uuringud.stream(), DummyData.randomUuringud().stream()).collect(Collectors.toList());
            }
            for (Uuring uuring : uuringud) {
                repo.addUuring(uuring);
            }
            Kriteerium kriteerium1 = new Kriteerium(80, 20, 50, 0, 2);
            Kriteerium kriteerium2 = new Kriteerium(80,20,56.6,0.2,3);

            ValimiSelekteerija<NimmelülidUuring> selekteerija1 = new ValimiSelekteerija<>(NimmelülidUuring.class, emf, kriteerium1);
            ValimiSelekteerija<NimmelülidUuring> selekteerija2 = new ValimiSelekteerija<>(NimmelülidUuring.class, emf, kriteerium2);

            Valim valim1 = selekteerija1.getValim();
            for (Uuring uuring : DummyData.randomUuringud(70)) {
                repo.addUuring(uuring);
            }
            Valim valim2 = selekteerija2.getValim();

            System.out.println(valim1);
            System.out.println(valim2);
            Assertions.assertTrue(valim1.isVastabKriteeriumitele());
            Assertions.assertTrue(valim2.isVastabKriteeriumitele());
        }
        catch (PuudulikValimException e) {
            System.out.println(e);
            System.out.println(e.getValim().toString());
            throw e;
        }
    }

    @Test
    public void töötabSuureValimiga() {
        System.out.println(repo.getAllUuringud(Uuring.class).size());
        for (int i = 0; i < 2000; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
            }
        }
        try {
            Kriteerium kriteerium = new Kriteerium(80, 20, 65, 2, 20);
            ValimiSelekteerija<NimmelülidUuring> selekteerija = new ValimiSelekteerija<>(NimmelülidUuring.class, emf, kriteerium);
            System.out.println(selekteerija.getValim());

        }
        catch (PuudulikValimException e) {
            System.out.println(e);
            System.out.println(e.getValim().toString());
        }
    }

    @RepeatedTest(5)
    public void töötabVäikeseValimiga() {
        System.out.println(repo.getAllUuringud(Uuring.class).size());
        for (int i = 0; i < 20; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
            }
        }
        try {
            Kriteerium kriteerium = new Kriteerium(80, 20, 65, 2, 10);
            ValimiSelekteerija<NimmelülidUuring> selekteerija = new ValimiSelekteerija<>(NimmelülidUuring.class, emf, kriteerium);
            System.out.println(selekteerija.getValim());
        }
        catch (PuudulikValimException e) {
            System.out.println(e);
            System.out.println(e.getValim().toString());
        }
    }

    @RepeatedTest(5)
    public void töötabMinimaalseKriteeriumiga() {
        System.out.println(repo.getAllUuringud(Uuring.class).size());
        for (int i = 0; i < 20; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
            }
        }
        try {
            Kriteerium kriteerium = new Kriteerium(60,5,10);
            ValimiSelekteerija<NimmelülidUuring> selekteerija = new ValimiSelekteerija<>(NimmelülidUuring.class, emf, kriteerium);
            System.out.println(selekteerija.getValim());
        }
        catch (PuudulikValimException e) {
            System.out.println(e);
            System.out.println(e.getValim().toString());
        }
    }
}
