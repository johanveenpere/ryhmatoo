import Model.NimmelülidUuring;
import Model.Uuring;
import Repository.UuringRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class SüsteemiliidesTest {

    static EntityManagerFactory emf;
    static EntityManager em;
    static Süsteemiliides liides;
    static UuringRepository db;

    @BeforeAll
    static void setUp(){
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
        db = new UuringRepository(emf, em);
        liides = new Süsteemiliides(db);
    }

    @AfterAll
    static void tearDown(){
        emf.close();
        em.close();
    }

    @Test
    void uusUuring() {
        String viit = "PiLdIvIiT";
        try {
            liides.UusUuring(viit, 100.0, NimmelülidUuring.class);
            assertNotNull(db.getUuring(viit));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void teeKokkuvõte() {

    }
}