import Model.NimmelülidUuring;
import Repository.UuringRepository;
import Service.Kriteerium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;



class SüsteemiliidesTest {

    static EntityManagerFactory emf;
    static Süsteemiliides liides;
    static UuringRepository db;

    @BeforeAll
    static void setUp(){
        var em = Persistence.createEntityManagerFactory("default").createEntityManager();
        db = new UuringRepository(em);
        liides = new Süsteemiliides(db);
    }

    @AfterAll
    static void tearDown(){
        emf.close();
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
        var test = LocalDate.of(2020,1,1);
        Kriteerium kriteerium = new Kriteerium(80,60,65,1,5, test);
        liides.teeKokkuvõte("testKokkuvõtted", NimmelülidUuring.class, kriteerium);
    }
}