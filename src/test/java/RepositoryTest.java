import Model.RindkereUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositoryTest {
    static EntityManagerFactory emf;
    static EntityManager em;
    static UuringRepository repo;

    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
        repo = new UuringRepository(em);
    }

    @AfterAll
    public static void destroy() {
        em.close();
        emf.close();
    }

    @BeforeEach
    public void clean() {
        em.getTransaction().begin();
        Query q4 = em.createQuery("DELETE from Uuring");
        q4.executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void uuringuidSaabLisada() {
        List<Uuring> uuringud = DummyData.randomUuringud();
        for (Uuring uuring : uuringud) {
            repo.addUuring(uuring);
        }
        List<Uuring> inDB = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList();
        Assertions.assertEquals(uuringud,inDB);
    }

    @Test
    public void uuringuidSaabEemaldadaObjektiJärgi() {
        List<Uuring> uuringud = DummyData.randomUuringud();
        for (Uuring uuring : uuringud) {
            repo.addUuring(uuring);
            repo.removeUuring(uuring);
        }
        List<Uuring> inDB = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList();
        Assertions.assertEquals(0,inDB.size());
    }

    @Test
    public void uuringuidSaabEemaldadaViidaJärgi() {
        List<Uuring> uuringud = DummyData.randomUuringud();
        for (Uuring uuring : uuringud) {
            repo.addUuring(uuring);
            repo.removeUuring(uuring.getViit());
        }
        List<Uuring> inDB = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList();
        Assertions.assertEquals(0,inDB.size());
    }

    @Test
    public void uuringuidSaabMuuta() {
        List<Uuring> uuringud = DummyData.randomUuringud();
        String[] viidad = new String[uuringud.size()];
        for (int i = 0; i < viidad.length; i++) {
            viidad[i] = uuringud.get(i).getViit();
        }
        List<Uuring> muudetudUuringud = DummyData.randomUuringud(viidad);

        for (Uuring uuring : muudetudUuringud) {
            repo.alterUuring(uuring);
        }
        List<Uuring> inDB = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList();
        for (int i = 0; i < Math.min(inDB.size(),muudetudUuringud.size()); i++) {
            Assertions.assertEquals(inDB.get(i).toString(),muudetudUuringud.get(i).toString());
        }
    }

    @Test
    public void saabKõikUuringud() {
        for (Uuring uuring : DummyData.randomUuringud()) {
            repo.addUuring(uuring);
        }
        Integer queryResult = Integer.parseInt(em.createQuery("SELECT COUNT(*) FROM Uuring").getSingleResult().toString());
        Integer repoResult = repo.getAllUuringud(Uuring.class).size();
        Assertions.assertEquals(queryResult, repoResult);
    }

    @Test
    public void kustutabKõikUuringud() {
        for (int i = 0; i < 10; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
            }
        }
        int sizeBefore = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList().size();
        Assertions.assertTrue(sizeBefore > 0);
        repo.deleteAll();
        int sizeAfter = em.createQuery("SELECT c FROM Uuring AS c", Uuring.class).getResultList().size();
        Assertions.assertEquals(0, sizeAfter);
    }

    @Test void päribMinimaalseKriteeriumiJärgi() {
        for (int i = 0; i < 10; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
            }
        }
        List<Uuring> uuringud = repo.getByKriteerium(RindkereUuring.class, new Kriteerium(10,60,5));
        Assertions.assertEquals(10,uuringud.size());
    }

    @Test
    public void päribTäitmiseJärgi() {
        for (int i = 0; i < 10; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                if (i % 2 == 0) {
                    uuring.setTäidetud(true);
                }
                repo.addUuring(uuring);
            }
        }
        Kriteerium kriteerium = new Kriteerium(10,60,5);
        kriteerium.setTäitmataUuringud(false);
        List<Uuring> uuringud = repo.getByKriteerium(RindkereUuring.class, kriteerium);
        Assertions.assertEquals(5,uuringud.size());
    }

    @Test
    public void päribKuupäevaJärgi() {
        for (int i = 0; i < 10; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
                if (i % 2 == 0) {
                    uuring.setLoomisaeg(LocalDate.of(2018,10,10));
                }
            }
        }
        List<Uuring> koikRindkereUuringudEnne2018 = repo.getByKriteerium(RindkereUuring.class, new Kriteerium(200,0,0,0,0,LocalDate.of(2017,5,12)));
        List<Uuring> koikRindkereUuringudPeale2018 = repo.getByKriteerium(RindkereUuring.class, new Kriteerium(200,0,0,0,0,LocalDate.of(2019,5,12)));
        Assertions.assertEquals(10, koikRindkereUuringudEnne2018.size());
        Assertions.assertEquals(5, koikRindkereUuringudPeale2018.size());
        System.out.println("=".repeat(10) + " Enne 2018 " + "=".repeat(10));
        for (Uuring uuring : koikRindkereUuringudEnne2018) {
            System.out.println(uuring.toString());
        }
        System.out.println("=".repeat(10) + " Pärast 2018 " + "=".repeat(10));
        for (Uuring uuring : koikRindkereUuringudPeale2018) {
            System.out.println(uuring.toString());
        }
    }

    @Test
    public void eiSaaLisadaKorduvaid() {
        Uuring uuring1 = new RindkereUuring("AB12345678",50);
        Uuring uuring2 = new RindkereUuring("AB12345678",60);
        repo.addUuring(uuring1);
        try {
            repo.addUuring(uuring2);
        }
        catch (EntityExistsException e) {
            System.out.println(e);
        }
        try {
            repo.addUuring(uuring2);
        }
        catch (EntityExistsException e) {
            System.out.println(e);
        }
    }

    @Test
    public void saaTäitmataUuringud() {
        List<Uuring> täidetudUuringud = new ArrayList<>();
        List<Uuring> täitmataUuringud = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            for (Uuring uuring : DummyData.randomUuringud()) {
                repo.addUuring(uuring);
                if (i % 2 == 0) {
                    uuring.setTäidetud(true);
                    täidetudUuringud.add(uuring);
                }
                else {
                    täitmataUuringud.add(uuring);
                }
            }
        }
        Assertions.assertArrayEquals(täitmataUuringud.toArray(), repo.getAllTäitmataUuringud().toArray());
    }
}
