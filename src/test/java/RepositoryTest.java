import Model.Uuring;
import Repository.UuringRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class RepositoryTest {
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
    public void uuringuidSaabEemaldada() {
        List<Uuring> uuringud = DummyData.randomUuringud();
        for (Uuring uuring : uuringud) {
            repo.addUuring(uuring);
            repo.removeUuring(uuring);
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
}
